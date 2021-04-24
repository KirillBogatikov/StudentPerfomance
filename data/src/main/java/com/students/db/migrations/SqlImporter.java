package com.students.db.migrations;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Discipline;
import com.students.db.model.Mark;
import com.students.db.model.Student;
import com.students.db.model.Teacher;
import com.students.db.repo.Database;
import com.students.db.sql.GroupRepository;
import com.students.db.sql.MarkRepository;
import com.students.db.sql.SqlRepository;
import com.students.db.sql.StudentRepository;
import com.students.db.sql.TeacherRepository;
import com.students.util.Hash;

public class SqlImporter extends SqlRepository {
	private String salt;
	private int saltPosition;

	public SqlImporter(String salt, int saltPosition, Database database) throws SQLException {
		super(database);

		database.execute("""
			CREATE TABLE IF NOT EXISTS "migrations" (
				"id" uuid PRIMARY KEY,
				"date" timestamp
			);
		""");

		this.salt = salt;
		this.saltPosition = saltPosition;
	}

	public boolean needImport() throws SQLException {
		return database.query(r -> 1, "SELECT 1 FROM \"migrations\" ORDER BY \"date\" DESC LIMIT 1") == null;
	}

	public void saveImport() throws SQLException {
		database.execute("INSERT INTO migrations (\"id\", \"date\") VALUES (?, ?)", UUID.randomUUID(), new Date(System.currentTimeMillis()));
	}
	
	public void importTeachers(List<Teacher> teachers) throws SQLException {
		var step = teachers.size() / 100;
		if (step == 0) {
			step = 1;
		}
		
		var repo = new TeacherRepository(database);
		for (int i = 0; i < teachers.size(); i++) {			
			var t = teachers.get(i);
			var auth = t.getAuth();

			auth.setPasswordHash(Hash.hash(auth.getPassword(), salt, saltPosition));
			repo.insert(t);
			
			if (i % step == 0) {
				System.out.printf("Teachers - %3.0f%%\n", ((float)i / teachers.size()) * 100);
			}
		}
	}

	public void importStudents(List<Student> students) throws SQLException {
		var step = students.size() / 100;
		if (step == 0) {
			step = 1;
		}
		
		var repo = new StudentRepository(database);
		for (int i = 0; i < students.size(); i++) {
			repo.insert(students.get(i));
			
			if (i % step == 0) {
				System.out.printf("Students - %3.0f%%\n", ((float)i / students.size()) * 100);
			}
		}
	}

	public void importGroups(List<ExtendedGroup> groups) throws SQLException {
		var step = groups.size() / 100;
		if (step == 0) {
			step = 1;
		}
		
		var repo = new GroupRepository(database);
		for (int i = 0; i < groups.size(); i++) {
			var g = groups.get(i);
			repo.insert(g);

			for (var id : g.getStudents()) {
				repo.moveStudent(UUID.randomUUID(), id, g.getId());
			}
			
			if (i % step == 0) {
				System.gc();
				System.out.printf("Groups - %3.0f%%\n", ((float)i / groups.size()) * 100);
			}
		}
	}
	
	public void importDiscipline(List<Discipline> disciplines) throws SQLException {
		var step = disciplines.size() / 100;
		if (step == 0) {
			step = 1;
		}
		
		var repo = new MarkRepository(database);
		for (int i = 0; i < disciplines.size(); i++) {
			repo.insertDiscipline(disciplines.get(i));
			
			if (i % step == 0) {
				System.out.printf("Groups - %3.0f%%\n", ((float)i / disciplines.size()) * 100);
			}
		}
	}
	
	public void importMarks(List<Mark> marks) throws SQLException {
		var step = marks.size() / 100;
		if (step == 0) {
			step = 1;
		}
		
		var repo = new MarkRepository(database);
		for (int i = 0; i < marks.size(); i++) {
			repo.insertMark(marks.get(i));
			
			if (i % step == 0) {
				System.out.printf("Marks - %3.0f%%\n", ((float)i / marks.size()) * 100);
			}
		}
	}
}
