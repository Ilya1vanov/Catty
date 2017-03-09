package com.ilya.ivanov.catty_catalog.model.user;

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

    public String getHumanReadableQuota() {
        if (quota >= 2E30)	// 1 GB
            return String.valueOf(quota / 2E30) + " GB";
        else if (quota >= 2E20)	// 1 MB
            return String.valueOf(quota / 2E20) + " MB";
        else if (quota >= 2E10)	// 1 KB
            return String.valueOf(quota / 2E10) + " KB";
        else
            return String.valueOf(quota) + " B";
    }

    /**
     * Subtract added files size from user's quota
     * @param filesSize positive number of bytes that must be less than this.getQuota()
     */
    public void subtractQuota(long filesSize) {
        this.quota -= filesSize;
    }

    public String getName() {
		return name;
	}

    /**
     * Created by Илья on 05.03.2017.
     */
    public enum UserType {
        admin, user, guest;

        String type() {
            return UserType.class.getTypeName();
        }
    }
}