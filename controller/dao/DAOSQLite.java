package com.ilya.ivanov.catty_catalog.controller.dao;

import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObjectFactory;
import com.ilya.ivanov.catty_catalog.model.user.AbstractUser;
import com.ilya.ivanov.catty_catalog.model.user.AbstractUserFactory;
import javafx.scene.control.TreeItem;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class DAOSQLite implements DAO<AbstractFileObject, AbstractUser> {
    private static final Logger log = Logger.getLogger(DAOSQLite.class.getName());

	private static final String JDBC_PREFIX = "jdbc:sqlite:";

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

    private PreparedStatement userQuery;
    private PreparedStatement userQuotaQuery;
    private PreparedStatement rootDirQuery;
    private String SQL_fileObjectQuery = "SELECT * FROM tree WHERE parentID = ?";

    private PreparedStatement updateFile;
    private PreparedStatement removeFiles;
    private PreparedStatement addFile;

    private PreparedStatement pullFile;

	@Override
	public void connect(String url) throws SQLException {
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
        } catch (SQLException e) {
            log.fatal("Cannot connect to database!", e);
            //throw new SQLException(e);
        }
        log.debug("Connected");
	}

	@Override
	public void disconnect() throws SQLException {
        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            log.fatal("Cannot disconnect from database!", e);
            throw new SQLException(e);
        }
        log.debug("Disconnected");
	}

	@Override
	public AbstractUser signIn(String login, String password) {
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
		return null;
	}

	@Override
	public TreeItem<AbstractFileObject> pullWorkingTree(String rootName) {
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
        }
        return root;
	}

	@Override
	public boolean deleteFileObjects(List<AbstractFileObject> abstractFileObjects) {
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
	}

    @Override
    public boolean pushFileObject(AbstractFileObject object, String path) {
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
        }

        return true;
    }

    @Override
	public boolean update(AbstractFileObject object) {
        try {
            updateFile.setString(1, object.getName());
            updateFile.setInt(2, object.getID());

            return updateFile.executeUpdate() != 0;
        } catch (SQLException e) {
            log.error("SQL Exception!", e);
            return false;
        }
    }

    /**
     * Recursive function that build sub tree according to the root param.
     * @param root Current root.
     * @return Sub tree.
     */
	private TreeItem<AbstractFileObject> pullSubTree(TreeItem<AbstractFileObject> root) {
	    ResultSet results;
        try {
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
        return root;
	}

    /**
     * Read the file and returns the byte array.
     * @param file Filepath.
     * @return The bytes of the file.
     */
    private byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("SQL Exception!", e);
        }
        return bos != null ? bos.toByteArray() : null;
    }

    public InputStream pullFile(AbstractFileObject file) {
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
        return data;
    }
}
