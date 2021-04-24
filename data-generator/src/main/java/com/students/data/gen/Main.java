package com.students.data.gen;

import java.io.File;
import java.io.FileInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Main {

	public static void main(String[] args) throws Exception {
		var gen = new Generator();

		var teachers = gen.generateTeachers(new FileInputStream("teachers.txt"));
		var students = gen.generateStudents(new FileInputStream("students.txt"));
		var groups = gen.generateGroups(students);
		
		var mapper = new ObjectMapper();	
		mapper.writer(SerializationFeature.INDENT_OUTPUT).writeValue(new File("teachers.json"), teachers);
		mapper.writer(SerializationFeature.INDENT_OUTPUT).writeValue(new File("students.json"), students);
		mapper.writer(SerializationFeature.INDENT_OUTPUT).writeValue(new File("groups.json"), groups);
	}

}
