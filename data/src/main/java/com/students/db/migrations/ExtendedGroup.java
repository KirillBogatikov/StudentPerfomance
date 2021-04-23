package com.students.db.migrations;

import java.util.UUID;

import com.students.db.model.Group;

public class ExtendedGroup extends Group {
	private UUID[] students;

	public UUID[] getStudents() {
		return students;
	}

	public void setStudents(UUID[] students) {
		this.students = students;
	}

}
