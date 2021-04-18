package com.students.db.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;

import com.students.db.repo.Database;
import com.students.util.Extractor;

public class SqlDatabase extends Database {
	private String url;

	public SqlDatabase(String url) {
		this.url = "jdbc:%s".formatted(url);
		try {
			Driver.register();
		} catch (Exception e) {}
		
		try {
			execute(Extractor.readText("sql/init.sql"));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Connection connect() throws SQLException {
		return DriverManager.getConnection(url);
	}
}
