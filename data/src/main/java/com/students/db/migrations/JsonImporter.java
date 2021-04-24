package com.students.db.migrations;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.students.db.model.Student;
import com.students.db.model.Teacher;
import com.students.util.Extractor;

public class JsonImporter {
	private ObjectMapper mapper = new ObjectMapper();
	
	private <T> List<T> generate(String file, TypeReference<List<T>> type) throws IOException {
		return mapper.readValue(Extractor.openStream("import/%s.json".formatted(file)), type);
	}
	
	public List<Teacher> generateTeachers() throws IOException {
		return generate("teachers", new TypeReference<List<Teacher>>(){});
	}
	
	public List<Student> generateStudents() throws IOException {
		return generate("students", new TypeReference<List<Student>>(){});
	}
	
	public List<ExtendedGroup> generateGroups() throws IOException {
		return generate("groups", new TypeReference<List<ExtendedGroup>>(){});
	}
}
