package com.students.db.migrations;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.students.db.migrations.api.Api;
import com.students.db.repo.Database;

public class Migrator {
	private Api api;
	private SqlImporter importer;
	
	public static final int TeachersCount = 20, StudentCount = 50, StudentsPerGroup = 25;
	
	public Migrator(String salt, int saltPosition, Database database) throws SQLException {
		api = new Api();
		importer = new SqlImporter(salt, saltPosition, database);
	}
	
	public void migrate() throws IOException, SQLException {
		long start = System.currentTimeMillis();
		
		if (!importer.needImport()) {
			System.out.println("Database state - actual. Skip migration");
			return;
		}
		
		System.out.printf("Generating %d teachers...\n", TeachersCount);
		var teachers = api.generateTeachers(TeachersCount);
		
		System.out.printf("Generating %d students...\n", StudentCount);
		var students = api.generateStudents(StudentCount);
		
		System.out.printf("Generating groups. Max students per group - %d...\n", StudentsPerGroup);
		var groups = api.generateGroups(StudentsPerGroup, students);
		System.out.printf("Generated %d groups\n", groups.size());
		
		System.out.printf("Generating disciplines...\n");
		var disciplines = api.generateDisciplines(teachers);
		System.out.printf("Generated %d disciplines...\n", disciplines.size());

		System.out.printf("Generating marks...\n");
		var marks = api.generateMarks(disciplines, groups);
		System.out.printf("Generated total %d marks...\n", marks.size());
		
		System.out.println("Inserting data");
		importer.importTeachers(teachers);
		importer.importStudents(students);
		importer.importGroups(groups);
		importer.importDiscipline(disciplines);
		importer.importMarks(marks);
		
		System.out.println("Saving meta");
		importer.saveImport();
		
		try(FileOutputStream fos = new FileOutputStream("teachers_backup.json")) {
			var mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(fos, teachers);
		}
		
		long end = System.currentTimeMillis() - start;
		System.out.printf("Migrated with %dm %ds %dms\n", end / 60000, (end / 1000) % 60, end % 1000);
	}
}
