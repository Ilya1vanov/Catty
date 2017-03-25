package model.user;

/**
 * Class represents Admin with read, write, execute and edit permissions.
 * Created by Илья on 05.03.2017.
 */
class Admin extends AbstractUser {
    Admin() {
        super(1, UserType.admin, "Admin", Long.MAX_VALUE);
    }

    /**
     * Admin has unlimited quota. So this method do nothing
     * @param fileSize anything
     */
    @Override
    public void subtractQuota(long fileSize) {}
}
