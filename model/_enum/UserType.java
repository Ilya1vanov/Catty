package com.ilya.ivanov.catty_cathalog.model._enum;

public enum UserType {
	ADMIN(), USER(), GUEST();
	
	String type() {
		return UserType.class.getTypeName();
	}
}
