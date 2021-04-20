package com.students.db.sql;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Mark;
import com.students.db.repo.Database;

import static com.students.util.Extractor.readText;

public class MarkRepository extends SqlRepository {
	private static final String DIR = "sql/plan/mark";
	private static final String getStudentMarks = readText(DIR, "by_student.sql"),
			getGroupMarks = readText(DIR, "by_group.sql");

	public MarkRepository(Database database) {
		super(database);
	}

	public List<Mark> listStudentMarks(UUID student) throws SQLException {
		return database.queryList(Mark.class, getStudentMarks, student);
	}

	public List<Mark> listGroupMarks(UUID group) throws SQLException {
		return database.queryList(Mark.class, getGroupMarks, group);
	}
	
	public boolean insertMark(Mark mark) throws SQLException {
		return database.execute(sql, args);
	}
}
