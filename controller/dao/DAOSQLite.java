package com.ilya.ivanov.catty_catalog.controller.dao;

import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import com.ilya.ivanov.catty_catalog.model.file.DirectoryObject;
import com.ilya.ivanov.catty_catalog.model.file.FileObject;
import com.ilya.ivanov.catty_catalog.model.user.AbstractUser;
import com.ilya.ivanov.catty_catalog.model.user.Admin;
import javafx.scene.control.TreeItem;

import java.sql.*;
import java.util.List;

public class DAOSQLite implements DAO<AbstractFileObject, AbstractUser> {
	private static Connection connection;
	private static Statement statement;
	private static ResultSet resultSet;

	@Override
	public void connect(String url) throws SQLException {
//		try {
//			Class.forName("org.sqlite.JDBC");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		connection = DriverManager.getConnection(url);
//		statement = connection.createStatement();

		// stub
	}

	@Override
	public void disconnect() throws SQLException {
		// stub
	}

	@Override
	public AbstractUser signIn(String login, String password) {
//		try {
//			BigInteger hash = new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(password.getBytes()));
//			resultSet = statement.executeQuery(
//					String.format("SELECT * FROM users WHERE login=\"%s\" and password=\"%s\"",
//							login, hash.toString(16)));
//
//			if (resultSet.next()) {
//				Model.user.setName(login);
//				//
//				resultSet.getString("type");
//				//MainStageController.user.setType();
//				return true;
//			}
//			else
//				return false;
//		} catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}
//		return false;

        // stub
		return new Admin(1, "Ilya");
	}

	@Override
	public TreeItem<AbstractFileObject> pullWorkingTree(String rootName) {
	    // stub

		TreeItem<AbstractFileObject> root1 = new TreeItem(new DirectoryObject("Dir1"));

		TreeItem<AbstractFileObject> root = new TreeItem(new DirectoryObject(rootName));
		root.setExpanded(true);
		root.getChildren().addAll(root1, new TreeItem(new DirectoryObject("Dir2")));

		root1.getChildren().addAll(
				new TreeItem(new FileObject(1, "File1", "", 12, 3)),
				new TreeItem(new FileObject(2, "File2", "", 28, 2)));
		return root;


//		TreeItem<AbstractFileObject> root = new TreeItem("/");
//		pullSubTree(root);
//		//root.getChildren().addAll(c);
//		// TODO Auto-generated method stub
	}

	@Override
	public boolean deleteFileObjects(List<AbstractFileObject> abstractFileObjects) {
		return false; // stub
	}

	@Override
	public boolean pushFileObjects(List<AbstractFileObject> files) {
		return false; // stub
	}

	private TreeItem<AbstractFileObject> pullSubTree(TreeItem<AbstractFileObject> root) {
		try {
			resultSet = statement.executeQuery("SELECT * FROM tree WHERE parentID=0");
			while(resultSet.next()) {
				root.getChildren().add(new TreeItem<>(new DirectoryObject(resultSet.getString("filename"))));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
