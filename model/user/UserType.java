package com.ilya.ivanov.catty_catalog.model.user;

/**
 * Created by Илья on 05.03.2017.
 */
public enum UserType {
    admin, user, guest;

    String type() {
        return UserType.class.getTypeName();
    }
}
