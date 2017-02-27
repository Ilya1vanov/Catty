package com.ilya.ivanov.catty_cathalog.model.implementation;

import com.ilya.ivanov.catty_cathalog.model._enum.UserType;

public class User {
	UserType type = UserType.GUEST;
	String name = "Guest";
	
	public UserType getType() {
		return type;
	}
	public void setType(UserType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
