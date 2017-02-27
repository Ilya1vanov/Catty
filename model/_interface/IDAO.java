package com.ilya.ivanov.catty_cathalog.model._interface;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.TreeItem;


/**
 * 
 * @author ilya
 *
 * @param <FileObject> is a class that represents files in storage
 */
public interface IDAO<FileObject> {
	/**
	 * @throws SQLException if a database access error occurs
	 * 
	 */
	public boolean connect() throws SQLException;
	
	/**
	 * verify user with given login and password to access storage
	 * @param login string that represents login (login.length() > 0)
	 * @param password string that represents the cleartext password
	 * @return true if user exists and password is right;false otherwise
	 */
	public boolean signIn(String login, String password);
	
	/**
	 * pull directories tree from storage
	 * @return root element of directories hierarchy
	 */
	public TreeItem<FileObject> pullWorkingTree();
	
	/**
	 * pull file from storage
	 * @return byte array which represents file as a binary
	 */
	public byte[] pullFile();
	
	/**
	 * conduct a transaction in the storage in order to remove files
	 * @param fileObjects
	 * @return true if and only if this file objects are successfully
     * deleted; false otherwise
	 */
	public boolean deleteFileObjects(List<FileObject> fileObjects);
	
	/**
	 * try to add all files to the storage
	 * @param files 
	 * @return 
	 */
	public boolean pushFileObjects(List<File> files);
}
