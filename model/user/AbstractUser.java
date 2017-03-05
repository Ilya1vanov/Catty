package com.ilya.ivanov.catty_catalog.model.user;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class AbstractUser {
    protected long ID;
	protected UserType type;
	protected String name;
	protected long quota;

    protected AbstractUser(long ID, UserType type, String name, long quota) {
        this.ID = ID;
        this.type = type;
        this.name = name;
        this.quota = quota;
    }

    public long getID() {
        return ID;
    }

    public UserType getType() {
        return type;
    }

    public long getQuota() {
        return quota;
    }

    public String getName() {
		return name;
	}
}

