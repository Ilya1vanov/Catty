package com.ilya.ivanov.catty_catalog.model.user;

/**
 * Created by Илья on 05.03.2017.
 */
public class Guest extends AbstractUser {

    public Guest() {
        super(0, UserType.guest, "Guest", 0);
    }
}
