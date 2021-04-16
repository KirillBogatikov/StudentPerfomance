package com.students.db.sql;

import java.sql.SQLException;
import java.util.UUID;

import com.students.db.model.Auth;
import com.students.db.model.PersonalData;
import com.students.db.repo.Database;
import com.students.util.Extractor;

public class UserDataRepository extends SqlRepository {
	private static final String getByLogin = Extractor.readText("sql/auth/get_by_login.sql"),
								getById = Extractor.readText("sql/auth/get_by_id.sql"),
								has = Extractor.readText("sql/auth/has.sql");
	
	public UserDataRepository(Database database) {
		super(database);
		mapping.register(Auth.class, r -> {
			var auth = new Auth();
			auth.setId(UUID.fromString(r.getString("auth_id")));
			auth.setLogin(r.getString("auth_login"));
			auth.setPasswordHash(r.getBytes("auth_password"));
			return auth;
		});
		mapping.register(PersonalData.class, r -> {
			var data = new PersonalData();
			data.setId(UUID.fromString(r.getString("data_id")));
			data.setFirstName(r.getString("data_first_name"));
			data.setLastName(r.getString("data_last_name"));
			data.setPatronymic(r.getString("data_patronymic"));
			return data;
		});
	}
	
	public Auth get(String login) throws SQLException {
		return database.query(Auth.class, getByLogin, login);
	}
	
	public Auth get(UUID id) throws SQLException {
		return database.query(Auth.class, getById, id);
	}

	public boolean has(UUID userId) throws SQLException {
		return database.query(r -> true, has, userId) != null;
	}
	
}
