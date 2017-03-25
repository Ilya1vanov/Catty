package model.file;

import java.io.File;

public class FileObject extends AbstractFileObject {
    public FileObject(File file, int parentID) {
		super(file, parentID);
	}

    FileObject(int ID, int parentID, String filename, String owner, long size, String uploaded) {
        super(ID, parentID, filename, owner, size, uploaded);
    }
}
