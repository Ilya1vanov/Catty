package com.ilya.ivanov.catty_cathalog.model.implementation;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.ilya.ivanov.catty_cathalog.controller.MainSceneController;
import com.ilya.ivanov.catty_cathalog.model._abstract.AbstractFileObject;
import com.ilya.ivanov.catty_cathalog.model._interface.IDAO;

import javafx.scene.control.TreeItem;

public class DAODatabase implements IDAO<AbstractFileObject> {
	private static Connection connection;
	private static Statement statement;
	private static ResultSet resultSet;

	@Override
	public boolean signIn(String login, String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			
			BigInteger hash = new BigInteger(1, digest.digest(password.getBytes()));	
			resultSet = statement.executeQuery("SELECT * FROM users WHERE login=\"" + login + "\" and password=\"" + hash.toString(16) +"\"");
			
			if (resultSet.next()) {
				MainSceneController.user.setName(login);
				//
				resultSet.getString("type");
				//MainSceneController.user.setType();
				return true;
			}
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public byte[] pullFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean connect() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection = DriverManager.getConnection("jdbc:sqlite:src/com/ilya/ivanov/catty_cathalog/resources/db");
		statement = connection.createStatement();
		return true;
	}

	@Override
	public boolean deleteFileObjects(List<AbstractFileObject> fileObjects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pushFileObjects(List<File> files) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TreeItem<AbstractFileObject> pullWorkingTree() {
		TreeItem<AbstractFileObject> root = new TreeItem("/");
		pullSubTree(root);
		//root.getChildren().addAll(c);
		// TODO Auto-generated method stub
		return null;
	}

	private TreeItem<AbstractFileObject> pullSubTree(TreeItem<AbstractFileObject> root) {
		try {
			resultSet = statement.executeQuery("SELECT * FROM tree WHERE parentID=0");
			while(resultSet.next()) {
				root.getChildren().add(new TreeItem<>(new DirectoryObject(resultSet.getString("name"))));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
