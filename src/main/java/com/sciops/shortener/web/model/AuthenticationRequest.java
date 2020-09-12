package com.sciops.shortener.web.model;

public class AuthenticationRequest {
	
	private final String userName;
	private final String password;
	
	public AuthenticationRequest(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
	
}
