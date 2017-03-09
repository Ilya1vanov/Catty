package com.ilya.ivanov.catty_catalog.model.file;

import java.io.File;

public class FileObject extends AbstractFileObject {
    private long parentID;

    public long getParentID() {
        return parentID;
    }

    public FileObject(File file, int parentID) {
		super(file);
        this.parentID = parentID;
		// TODO Auto-generated constructor stub
	}

    public FileObject(int ID, String name, String path, long size, long parentID) {
        super(ID, name, path, size);
        this.parentID = parentID;
    }
}
