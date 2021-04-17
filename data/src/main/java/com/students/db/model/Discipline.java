package com.students.db.model;

import java.util.UUID;

public class Discipline {
	private UUID id;
	private String name;
	
	public Discipline() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
