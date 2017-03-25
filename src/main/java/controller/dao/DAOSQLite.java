package controller.dao;

import javafx.scene.control.TreeItem;
import model.file.AbstractFileObject;
import model.file.AbstractFileObjectFactory;
import model.user.AbstractUser;
import model.user.AbstractUserFactory;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("JpaQueryApiInspection")
public class DAOSQLite implements DAO<AbstractFileObject, AbstractUser> {
    private static final Logger log = Logger.getLogger(DAOSQLite.class.getName());

	private static final String JDBC_PREFIX = "jdbc:sqlite:";

    // make this lock fair to support starving protection
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

    private PreparedStatement userQuery;
    private PreparedStatement userQuotaQuery;
    private PreparedStatement rootDirQuery;

    private PreparedStatement updateFile;
    private PreparedStatement removeFiles;
    private PreparedStatement addFile;

    private PreparedStatement pullFile;

    public boolean isConnected() {
        return connection == null;
    }

	@Override
	public void  connect(String url) throws SQLException {
        rwLock.writeLock().lock();
        SQLException exception = null;

        try {
            connection = DriverManager.getConnection(JDBC_PREFIX + url);
            statement = connection.createStatement();
            userQuery = connection.prepareStatement("SELECT * FROM users WHERE login = ? and password = ?");
            userQuotaQuery = connection.prepareStatement("SELECT size FROM tree WHERE uploaded = ? and owner = ?");

            rootDirQuery = connection.prepareStatement("SELECT * FROM tree WHERE name = ? and parentID = ?");

            updateFile = connection.prepareStatement("UPDATE or IGNORE tree SET name = ? WHERE ID = ?");
            removeFiles = connection.prepareStatement("DELETE FROM tree WHERE id = ?");
            addFile = connection.prepareStatement("INSERT OR IGNORE INTO tree (parentID, name, owner, size, uploaded, data) VALUES(?, ?, ?, ?, ?, ?)");
            pullFile = connection.prepareStatement("SELECT data FROM tree WHERE ID = ?");

            log.debug("Connected");
            } catch (SQLException e) {
                log.fatal("Database connection error occurred!", e);
                exception = e;
            } finally {
                rwLock.writeLock().unlock();
                if (exception != null)
                    throw exception;
            }
        }

	@Override
	public void disconnect() throws SQLException {
	    rwLock.writeLock().lock();
        try {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();

            log.debug("Disconnected");

            connection = null;
            statement = null;
            resultSet = null;
        } catch (SQLException e) {
            log.fatal("Database disconnection error occurred!", e);
            throw e;
        }
        finally {
            rwLock.writeLock().unlock();
        }
	}

	@Override
	public AbstractUser signIn(String login, String password) {
	    rwLock.readLock().lock();
		try {
			BigInteger hash = new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(password.getBytes()));

            userQuery.setString(1, login);
            userQuery.setString(2, hash.toString(16));
            resultSet = userQuery.executeQuery();

			if (resultSet.next()) {
			    long uploadedToday = 0;
                long id = resultSet.getLong("ID");

                userQuotaQuery.setString(1, AbstractFileObject.getDateFormat().get().format(new Date()));
                userQuotaQuery.setString(2, login);
                resultSet = userQuotaQuery.executeQuery();

                while(resultSet.next())
                    uploadedToday += resultSet.getLong("size");

                return AbstractUserFactory.getInstance(id, login, uploadedToday);
			}
		} catch (Exception e) {
            log.error("SQL Exception!", e);
		}
		finally {
            rwLock.readLock().unlock();
        }
        return null;
	}

	@Override
	public TreeItem<AbstractFileObject> pullWorkingTree(String rootName) {
	    rwLock.readLock().lock();
        TreeItem<AbstractFileObject> root = null;

        try {
            rootDirQuery.setString(1, rootName);
            rootDirQuery.setInt(2, 0);
            resultSet = rootDirQuery.executeQuery();

            if (resultSet.next())
                root = new TreeItem(AbstractFileObjectFactory.getInstance(resultSet.getInt("ID"), 0, rootName, null, 0, null));
            else
                return null;

            // recursion entrance point
            pullSubTree(root);
        } catch (SQLException e) {
            log.error("SQL Exception!", e);
            root = null;
        }
        finally {
            rwLock.readLock().unlock();
        }
        return root;
	}

	@Override
	public boolean deleteFileObjects(List<AbstractFileObject> abstractFileObjects) {
	    rwLock.writeLock().lock();
        try {
            for (AbstractFileObject object : abstractFileObjects) {
                removeFiles.setInt(1, object.getID());
                removeFiles.execute();
            }

            return true;
        } catch (SQLException e) {
            log.error("SQL Exception!", e);
            return false;
        }
        finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public boolean pushFileObject(AbstractFileObject object, String path) {
        rwLock.writeLock().lock();
        boolean returnStatement = true;

        try {
            addFile.setInt(1, object.getParentID());
            addFile.setString(2, object.getName());
            if (path == null) {
                addFile.setString(3, null);
                addFile.setInt(4, 0);
                addFile.setString(5, null);
                addFile.setBytes(6, null);
            }
            else {
                addFile.setString(3, object.getOwner());
                addFile.setInt(4, (int) object.getLongSize());
                addFile.setString(5, object.getUploaded());
                addFile.setBytes(6, readFile(path));
            }
            addFile.execute();
        } catch (SQLException e) {
            log.error("SQL Exception!", e);
            returnStatement = false;
        }
        finally {
            rwLock.writeLock().unlock();
        }

        return returnStatement;
    }

    @Override
	public boolean update(AbstractFileObject object) {
        rwLock.writeLock().lock();

        try {
            updateFile.setString(1, object.getName());
            updateFile.setInt(2, object.getID());

            return updateFile.executeUpdate() != 0;
        } catch (SQLException e) {
            log.error("SQL Exception!", e);
            return false;
        }
        finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Recursive function that build sub tree according to the root param.
     * @param root Root to pull.
     * @return Sub tree.
     */
	private TreeItem<AbstractFileObject> pullSubTree(TreeItem<AbstractFileObject> root) {
	    rwLock.readLock().lock();

	    ResultSet results;
        try {
            String SQL_fileObjectQuery = "SELECT * FROM tree WHERE parentID = ?";
            PreparedStatement stm = connection.prepareStatement(SQL_fileObjectQuery);
            stm.setInt(1, root.getValue().getID());
            results = stm.executeQuery();

            while(results.next()) {
                TreeItem<AbstractFileObject> item = new TreeItem<>(AbstractFileObjectFactory.getInstance(
                        results.getInt("ID"),
                        results.getInt("parentID"),
                        results.getString("name"),
                        results.getString("owner"),
                        results.getInt("size"),
                        results.getString("uploaded"))
                );

                if (item.getValue().isDir())
                    pullSubTree(item);

                root.getChildren().add(item);
            }
        } catch (SQLException e) {
            log.error("SQL Exception!", e);
        }
        finally {
            rwLock.readLock().unlock();
        }

        return root;
	}

    /**
     * Read the file and returns the byte array.
     * @param file Filepath.
     * @return The bytes of the file.
     */
    private byte[] readFile(String file) {
        ByteArrayOutputStream bos;
        File f = new File(file);

        try {
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("File read exception! [file=" + f.getName() + "]", e);
            bos = null;
        }
        return bos != null ? bos.toByteArray() : null;
    }

    public InputStream pullFile(AbstractFileObject file) {
        rwLock.readLock().lock();
        InputStream data = null;
        try {
            pullFile.setInt(1, file.getID());
            resultSet = pullFile.executeQuery();

            if (resultSet.next()) {
                data = resultSet.getBinaryStream("data");
            }
        } catch (SQLException e) {
            log.warn("Cannot pull file!", e);
        }
        finally {
            rwLock.readLock().unlock();
        }
        return data;
    }
}
