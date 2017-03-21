package com.ilya.ivanov.catty_catalog.model.user;

/**
 * Created by Илья on 05.03.2017.
 */
public class AbstractUserFactory {
    public static AbstractUser getInstance(long ID, String name, long uploadedToday) {
        if (name.equals("admin"))
            return new Admin(ID);

        return new User(ID, name, User.DEFAULT_QUOTA - uploadedToday);
    }
}
