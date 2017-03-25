package model.user;

/**
 * File object factory. Adapted for database.
 * Created by Илья on 05.03.2017.
 */
public class AbstractUserFactory {
    /**
     * Parse name and instance UserObject. If name.equals(""admin"")
     * return Admin(). Returns User() otherwise.
     * @param ID unique ID
     * @param name username
     * @param uploadedToday up-to-date quota for uploading files
     * @return If name.equals(""admin"")
     * return Admin(). Returns User() otherwise.
     */
    public static AbstractUser getInstance(long ID, String name, long uploadedToday) {
        if (name.equals("admin"))
            return new Admin();

        return new User(ID, name, User.DEFAULT_QUOTA - uploadedToday);
    }
}
