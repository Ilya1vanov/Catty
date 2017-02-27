package com.ilya.ivanov.catty_cathalog.model._enum;

public enum User {
	ADMIN() {
		{
			name = "admin";
		}
	}, USER() {
		{
			name = "user";
		}
		
	}, GUEST() {
		{
			name = "guest";
		}
	};
	
	String name;
	// permissions
	
	public String getName() {
		return name;
	}
	
	private User(String name) {
		this.name = name;
	}
	
	private User() {
		
	}
}
