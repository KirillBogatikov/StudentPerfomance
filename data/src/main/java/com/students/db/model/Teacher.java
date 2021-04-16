package com.students.db.model;

import java.util.UUID;

public class Teacher {
	private UUID id;
	private Auth auth;
	private PersonalData data;
	
	public Teacher() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public PersonalData getData() {
		return data;
	}

	public void setData(PersonalData data) {
		this.data = data;
	}	
}
