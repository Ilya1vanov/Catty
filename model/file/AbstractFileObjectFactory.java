package com.ilya.ivanov.catty_catalog.model.file;

/**
 * Created by Илья on 05.03.2017.
 */
public class AbstractFileObjectFactory {
    public static AbstractFileObject getInstance(int ID, int parentID, String name, String owner, long size, String date) {
        if (owner == null || date == null) {
            return new DirectoryObject(ID, parentID, name);
        }
        else {
            return new FileObject(ID, parentID, name, owner, size, date);
        }
    }
}
