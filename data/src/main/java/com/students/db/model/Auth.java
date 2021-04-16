package com.students.db.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Auth {
	private UUID id;
	private String login;
	@JsonIgnore
	private byte[] passwordHash;
	private String password;
	
	public Auth() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public byte[] getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(byte[] password) {
		this.passwordHash = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
