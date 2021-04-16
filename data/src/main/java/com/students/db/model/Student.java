package com.students.db.model;

import java.util.UUID;

public class Student {
	private UUID id;
	private PersonalData data;
	private Contact contact;
	
	public Student() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public PersonalData getData() {
		return data;
	}

	public void setData(PersonalData data) {
		this.data = data;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
