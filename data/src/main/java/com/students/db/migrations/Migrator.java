package com.students.db.migrations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.students.db.model.Student;
import com.students.db.model.Teacher;
import com.students.db.repo.Database;

import me.randomuser.api.RandomUserApi;

public class Migrator {
	private RandomUserApi api;
	private SqlImporter importer;
	
	public static final int TeachersCount = 20, StudentCount = 50, StudentsPerGroup = 25;
	
	public Migrator(String salt, int saltPosition, Database database) throws SQLException {
		api = new RandomUserApi();
		importer = new SqlImporter(salt, saltPosition, database);
	}
	
	public void migrate(boolean russian) throws IOException, SQLException {
		long start = System.currentTimeMillis();
		
		if (!importer.needImport()) {
			System.out.println("Database state - actual. Skip migration");
			return;
		}
		
		List<Teacher> teachers;
		List<Student> students;
		List<ExtendedGroup> groups;
		
		if (russian) {
			var json = new JsonImporter();
			
			teachers = json.generateTeachers();
			students = json.generateStudents();
			groups = json.generateGroups();
			
			json = null;
			System.gc();
		} else {
			System.out.printf("Generating %d teachers...\n", TeachersCount);
			teachers = api.generateTeachers(TeachersCount);
			
			System.out.printf("Generating %d students...\n", StudentCount);
			students = api.generateStudents(StudentCount);
		
			System.out.printf("Generating groups. Max students per group - %d...\n", StudentsPerGroup);
			groups = api.generateGroups(StudentsPerGroup, students);
			System.out.printf("Generated %d groups\n", groups.size());
		}
		
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
		
		long end = System.currentTimeMillis() - start;
		System.out.printf("Migrated with %dm %ds %dms\n", end / 60000, (end / 1000) % 60, end % 1000);
	}
}
