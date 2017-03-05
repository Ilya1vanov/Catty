package com.ilya.ivanov.catty_catalog.model.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractFileObject {
	protected int ID;
	protected String filename;
	protected String path;
	protected long size;

	protected AbstractFileObject(File file) {
		path = file.getPath();
		filename = file.getName();
		try {
			size = Files.size(Paths.get(file.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public AbstractFileObject(int ID, String name, String path, long size) {
		this.ID = ID;
		this.filename = name;
		this.path = path;
		this.size = size;
	}

	// debug
	protected AbstractFileObject(String name) {
		this.filename = name;
	}

	public int getID() {
		return ID;
	}

	public String getFilename() {
		return filename;
	}

	public String getPath() {
		return path;
	}

	public String getSize() {
		// human readable
		if (size >= 2E30)	// 1 GB
			return String.valueOf(size / 2E30) + " GB";
		else if (size >= 2E20)	// 1 MB
			return String.valueOf(size / 2E20) + " MB";
		else if (size >= 2E10)	// 1 KB
			return String.valueOf(size / 2E10) + " KB";
		else
			return String.valueOf(size) + " B";
	}	
}
