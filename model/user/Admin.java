package com.ilya.ivanov.catty_catalog.model.user;

/**
 * Created by Илья on 05.03.2017.
 */
public class Admin extends AbstractUser {

    public Admin(long ID, String name) {
        super(ID, UserType.admin, name, Long.MAX_VALUE);
    }

    /**
     * Admin has unlimited quota. So this method do nothing
     * @param fileSize anything
     */
    @Override
    public void subtractQuota(long fileSize) {}
}
