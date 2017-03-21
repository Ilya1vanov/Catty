package com.ilya.ivanov.catty_catalog.model.file;

import java.io.File;
import java.sql.Date;

public class FileObject extends AbstractFileObject {
    public FileObject(File file, int parentID) {
		super(file, parentID);
	}

    protected FileObject(int ID, int parentID, String filename, String owner, long size, String uploaded) {
        super(ID, parentID, filename, owner, size, uploaded);
    }
}
