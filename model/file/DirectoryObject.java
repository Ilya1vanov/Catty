package com.ilya.ivanov.catty_catalog.model.file;


import java.io.File;
import java.sql.Date;

public class DirectoryObject extends AbstractFileObject {
    protected DirectoryObject(int ID, int parentID, String name) {
        super(ID, parentID, name, null, 0, null);
    }

    public DirectoryObject(int parentID, String name) {
        super(-1, parentID, name, null, 0, null);
    }

    /**
	 * Directory has no size
	 * @return null
     */
	@Override
	public String getSize() {
		return null;
	}

    /**
     * Directory has no owner
     * @return null
     */
    @Override
    public String getOwner() {
        return null;
    }

    /**
     * Directory has no upload date
     * @return null
     */
    @Override
    public String getUploaded() {
        return null;
    }
}
