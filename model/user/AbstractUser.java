package com.ilya.ivanov.catty_catalog.model.user;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import static com.ilya.ivanov.catty_catalog.model.user.UserType.*;


public class AbstractUser {
    protected long ID;
    private UserType type;
    protected String name;
    private LongProperty quota = new SimpleLongProperty(this, "quota");

    protected AbstractUser(long ID, UserType type, String name, long quota) {
        this.ID = ID;
        this.type = type;
        this.name = name;
        this.quota.set(quota);
    }

    public long getID() {
        return ID;
    }

    public String getType() {
        return type.toString();
    }

    public long getQuota() {
        return quota.get();
    }

    public void resetQuota() {
        quota.set(User.DEFAULT_QUOTA);
    }

    public LongProperty quotaProperty() {
        return quota;
    }

    /**
     * Return size of quota in human-readable format (e.g. 1,5 BM; 375 KB).
     * @return Human-readable quota.
     */
    public String getHumanReadableQuota() {
        long _quota = quota.get();
        if (_quota >= 0x4000_0000)    // 1 GB
            return String.format("%.1f", ((double)_quota) / 0x4000_0000) + " GB";
        else if (_quota >= 0x10_0000)    // 1 MB
            return String.format("%.1f", ((double)_quota) / 0x10_0000) + " MB";
        else if (_quota >= 0x400)    // 1 KB
            return String.valueOf(_quota / 0x400) + " KB";
        else
            return String.valueOf(_quota) + " B";
    }

    /**
     * Subtract added files size from user's quota.
     * @param filesSize positive number of bytes that must be less than this.getQuota().
     */
    public void subtractQuota(long filesSize) {
        quota.set(quota.get() - filesSize);
    }

    /**
     * Returns username.
     * @return Username.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true if user is allowed to browse the storage data.
     * @return True if user is allowed to browse the storage data;
     * false otherwise
     */
    public boolean hasReadPerm() {
        return (type.permissions & read) == read;
    }

    /**
     * Returns true if user is allowed to add files to the storage. Also rename and remove his own files.
     * @return True if user is allowed to add files to the storage. Also rename and remove his own files.
     */
    public boolean hasWritePerm() {
        return (type.permissions & write) == write;
    }

    /**
     * Returns true if user is allowed to open files.
     * @return True if user is allowed to open files.
     */
    public boolean hasExecutePerm() {
        return (type.permissions & execute) == execute;
    }

    /**
     * Returns true if user is allowed to rename and remove other's files.
     * @return True if user is allowed to rename and remove other's files.
     */
    public boolean hasEditPerm() {
        return (type.permissions & edit) == edit;
    }
}