package com.students.db.model;

import java.util.UUID;

public class Mark {
	private UUID id;
	private Student student;
	private Discipline discipline;
	private int mark;
	
	public Mark() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}
}
