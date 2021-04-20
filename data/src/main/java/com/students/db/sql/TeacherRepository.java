package com.students.db.sql;

import static com.students.util.Extractor.readText;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Auth;
import com.students.db.model.PersonalData;
import com.students.db.model.Teacher;
import com.students.db.repo.Database;

public class TeacherRepository extends SqlRepository {
	private static final String DIR = "sql/teacher";
	private static final String get = readText(DIR, "get_by_id.sql"), getByAuth = readText(DIR, "get_by_auth.sql"),
			has = readText(DIR, "has.sql"), insert = readText(DIR, "insert.sql"), update = readText(DIR, "update.sql"),
			delete = readText(DIR, "delete.sql"), list = readText(DIR, "list.sql"),
			search = readText(DIR, "search.sql");

	public TeacherRepository(Database database) {
		super(database);
		mapping.register(Teacher.class, r -> {
			var t = new Teacher();
			t.setId(UUID.fromString(r.getString("teacher_id")));
			t.setAuth(mapping.forType(Auth.class).process(r));
			t.setData(mapping.forType(PersonalData.class).process(r));
			return t;
		});
	}

	public List<Teacher> list(int offset, int limit) throws SQLException {
		return database.queryList(Teacher.class, list, offset, limit);
	}

	public List<Teacher> search(String query, int offset, int limit) throws SQLException {
		return database.queryList(Teacher.class, search, query, offset, limit);
	}

	public Teacher get(UUID id) throws SQLException {
		return database.query(Teacher.class, get, id);
	}

	public Teacher getByAuthId(UUID id) throws SQLException {
		return database.query(Teacher.class, getByAuth, id);
	}

	public boolean update(Teacher t) throws SQLException {
		if (database.query(r -> 1, get, t.getId()) == null) {
			return false;
		}

		var auth = t.getAuth();
		var data = t.getData();
		database.execute(update, auth.getLogin(), auth.getPasswordHash(), auth.getId(), data.getFirstName(),
				data.getLastName(), data.getPatronymic(), data.getId());
		return true;
	}

	public void insert(Teacher t) throws SQLException {
		var auth = t.getAuth();
		var data = t.getData();
		database.execute(insert, auth.getId(), auth.getLogin(), auth.getPasswordHash(), data.getId(),
				data.getFirstName(), data.getLastName(), data.getPatronymic(), t.getId(), auth.getId(), data.getId());
	}

	public boolean delete(UUID id) throws SQLException {
		var t = get(id);
		if (t == null) {
			return false;
		}

		database.execute(delete, t.getId(), t.getAuth().getId(), t.getData().getId());
		return true;
	}

	public boolean has(UUID id) throws SQLException {
		return database.query(r -> 1, has, id) != null;
	}
}
