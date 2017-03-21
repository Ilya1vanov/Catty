package com.ilya.ivanov.catty_catalog.model.user;

/**
 * This enum represents types of accounts that can exist
 * in this catalog.
 * Created by Илья on 05.03.2017.
 */
enum UserType {
    admin(UserType.read | UserType.execute | UserType.write | UserType.edit),
    user(UserType.read | UserType.execute | UserType.write),
    guest(UserType.read);

    // allows user to browse the data
    static final int read = 0x1;
    //allows user to open files
    static final int execute = 0x2;
    // allows user to add files; rename and remove his own files
    static final int write = 0x4;
    // allows user to remove all files
    static final int edit = 0x8;

    int permissions;

    UserType(int permissions) {
        this.permissions = permissions;
    }
}