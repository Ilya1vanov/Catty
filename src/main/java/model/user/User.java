package model.user;

/**
 * Class represents User with read, write and execute permissions.
 * Created by Илья on 05.03.2017.
 */
class User extends AbstractUser {
    static final long DEFAULT_QUOTA = 10485760;

    User(long ID, String name, long quota) {
        super(ID, UserType.user, name, quota);
    }
}
