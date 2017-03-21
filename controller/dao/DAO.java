package com.ilya.ivanov.catty_catalog.controller.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import javafx.scene.control.TreeItem;


/**
 * Interface provide interaction with a tree structure storage
 * @author ilya
 *
 * @param <StoredObject> class that represents object to store
 * @param <UserObject> class that represents user of storage
 */
public interface DAO<StoredObject, UserObject> {
	/**
	 * Connect to the storage with the given url.
	 * May be not overridden if storage is local.
     * @param url a database url
	 * @throws Exception to provide more information about error
     */
	default void connect(String url) throws Exception {}

    /**
     * Disconnect form the storage.
     * May be not overridden if storage is local.
     * @throws Exception to provide more information about error
     */
	default void disconnect() throws Exception {}

	/**
	 * Verify user with given login and password to access storage
	 * @param login string that represents login
	 * @param password string that represents the cleartext password
	 * @return user object; null if there are no such user or wrong password
	 */
    UserObject signIn(String login, String password);

	/**
	 * Pull directories tree from storage
	 * @return root element of directories hierarchy
	 */
	TreeItem<StoredObject> pullWorkingTree(String rootName);

	/**
	 * Conduct a transaction in the storage in order to remove files
	 * @param fileObjects
	 * @return True if and only if transaction succeeded
	 */
	boolean deleteFileObjects(List<StoredObject> fileObjects);
	
	/**
	 * Try to add all files to the storage.
	 * @param object List with objects to store.
	 * @return True if and only if transaction succeeded.
	 */
	boolean pushFileObject(StoredObject object, String path);

	/**
	 * Update fields of StoredObject with the given ID.
	 * @param object Instance of the StoredObject.
	 * @return true if object updated successfully; false otherwise.
     */
	boolean update(StoredObject object);

	/**
	 * Pulls stored file object from storage and returns as a binary stream array.
	 * @param file Object of stored file to pull.
	 * @return OutputStream that represent stored file.
     */
	public InputStream pullFile(StoredObject file);
}
