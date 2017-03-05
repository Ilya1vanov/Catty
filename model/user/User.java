package com.ilya.ivanov.catty_catalog.model.user;

/**
 * Created by Илья on 05.03.2017.
 */
public class User extends AbstractUser {
    public User(long ID, String name, long quota) {
        super(ID, UserType.user, name, quota);
    }
}
