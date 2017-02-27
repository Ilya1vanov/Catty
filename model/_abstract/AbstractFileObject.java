package com.ilya.ivanov.catty_cathalog.model._abstract;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractFileObject {
	private String name;
	private long size;
	
	protected AbstractFileObject(File file) {
		name = file.getName();
		try {
			size = Files.size(Paths.get(file.getAbsolutePath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		// human readable
		return String.valueOf(size);
	}

	public void setSize(long size) {
		this.size = size;
	}
	
}
