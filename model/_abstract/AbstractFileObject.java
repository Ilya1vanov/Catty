package com.ilya.ivanov.catty_cathalog.model._abstract;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractFileObject {
	protected String name;
	protected long size;
	
	protected AbstractFileObject(File file) {
		name = file.getName();
		try {
			size = Files.size(Paths.get(file.getAbsolutePath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected AbstractFileObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getSize() {
		// human readable
		return String.valueOf(size);
	}	
}
