package com.ilya.ivanov.catty_catalog.model.file;

import com.ilya.ivanov.catty_catalog.model.Model;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class AbstractFileObject implements Cloneable {
	protected int ID;
    protected int parentID;
	protected String name;
	protected String owner;
    protected long size;
	protected Date uploaded;

    // date format
    static private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Returns current date representation format.
     * @return Date format.
     */
    public static ReadOnlyObjectWrapper<DateFormat> getDateFormat() {
        return new ReadOnlyObjectWrapper<>(dateFormat);
    }

    /**
     * Creates AbstractFileObject instance from File.
     * @param file Filesystem object.
     */
    protected AbstractFileObject(File file, int parentID) {
        this.parentID = parentID;
	    owner = Model.getUser().getName();
        name = file.getName();
		try {
			size = Files.size(Paths.get(file.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		uploaded = new Date();
	}

    public AbstractFileObject(int ID, int parentID, String filename, String owner, long size, String uploaded) {
        this.ID = ID;
        this.parentID = parentID;
        this.name = filename;
        this.owner = owner;
        this.size = size;
        try {
            if (uploaded != null)
                this.uploaded = dateFormat.parse(uploaded);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getID() {
		return ID;
	}

    public int getParentID() {
        return parentID;
    }

    public String getName() {
		return name;
	}

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
		return owner;
	}

    /**
     * Returns date of upload as a String with format returned by AbstractFileObject.dateFormat().
     * @return Formatted date.
     */
    public String getUploaded() {
        if (uploaded == null)
            return null;
        return dateFormat.format(uploaded);
    }

    /**
     * Returns String that represents human-readable size(e.g. 1,4 MB; 36 KB)
     * @return String that represents human-readable size.
     */
    public String getSize() {
		// human readable
		if (size >= 0x4000_0000)	// 1 GB
			return String.format("%.1f", ((double)size) / 0x4000_0000) + " GB";
		else if (size >= 0x10_0000)	// 1 MB
			return String.format("%.1f", ((double)size) / 0x10_0000) + " MB";
		else if (size >= 0x400)	// 1 KB
			return String.valueOf(size / 0x400) + " KB";
		else
			return String.valueOf(size) + " B";
	}

	public long getLongSize() {
	    return size;
    }

	public boolean isDir() {
		return uploaded == null || owner == null;
	}

	public boolean isFile() {
		return !isDir();
	}

	// debug
	@Override
	public String toString() {
		return "AbstractFileObject{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", owner='" + owner + '\'' +
				", size=" + size +
				'}';
	}

	public AbstractFileObject clone() {
        try {
            return (AbstractFileObject)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
