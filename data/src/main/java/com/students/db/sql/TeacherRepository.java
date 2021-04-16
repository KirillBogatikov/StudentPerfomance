package com.students.db.sql;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Auth;
import com.students.db.model.PersonalData;
import com.students.db.model.Teacher;
import com.students.db.repo.Database;
import com.students.util.Extractor;

public class TeacherRepository extends SqlRepository {
	private static final String DIR = "sql/teacher";
	private static final String get = Extractor.readText(DIR, "get_by_id.sql"),
			insert = Extractor.readText(DIR, "insert.sql"), update = Extractor.readText(DIR, "update.sql"),
			delete = Extractor.readText(DIR, "delete.sql"), list = Extractor.readText(DIR, "list.sql"),
			search = Extractor.readText(DIR, "search.sql");

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

	public UUID save(Teacher t) throws SQLException {
		var auth = t.getAuth();
		var data = t.getData();
		UUID id = t.getId(), authId = auth.getId(), dataId = data.getId();
		
		var sql = update;
		var args = new Object[] {
			auth.getLogin(), auth.getPasswordHash(), auth.getId(), 
			data.getFirstName(), data.getLastName(), data.getPatronymic(), data.getId()
		};
		
		if (id == null) {
			id = UUID.randomUUID();
			authId = UUID.randomUUID();
			dataId = UUID.randomUUID();
			sql = insert;
			args = new Object[] {
				authId, auth.getLogin(), auth.getPasswordHash(),  
				dataId, data.getFirstName(), data.getLastName(), data.getPatronymic(), 
				id, authId, dataId
			};
		}
		
		database.execute(sql, args);
		
		t.setId(id);
		t.getAuth().setId(authId);
		t.getData().setId(dataId);
		return id;
	}

	public void delete(UUID id) throws SQLException {
		var t = get(id);
		database.execute(delete, t.getId(), t.getAuth().getId(), t.getData().getId());
	}

}
