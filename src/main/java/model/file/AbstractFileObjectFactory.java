package model.file;

/**
 * User factory. Adapted for database.
 * Created by Илья on 05.03.2017.
 */
public class AbstractFileObjectFactory {
    /**
     * Creates FileObject. If owner == null || size == 0 || date == null
     * returns DirectoryObject. FileObject otherwise.
     * @param ID unique ID
     * @param parentID  ID of parent element
     * @param name  name of file of directory
     * @param owner file owner
     * @param size file size
     * @param date uploading date
     * @return File object
     */
    public static AbstractFileObject getInstance(int ID, int parentID, String name, String owner, long size, String date) {
        if (owner == null || date == null) {
            return new DirectoryObject(ID, parentID, name);
        }
        else {
            return new FileObject(ID, parentID, name, owner, size, date);
        }
    }
}
