package com.students.db.sql;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.Driver;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.students.db.repo.Database;
import com.students.util.Extractor;

public class SqlDatabase extends Database {
	private ComboPooledDataSource dataSource;

	public SqlDatabase(String url) {
		try {
			Driver.register();
		} catch (Exception e) {}

		dataSource = new ComboPooledDataSource();
		
		try {
			dataSource.setDriverClass("org.postgresql.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		
		dataSource.setJdbcUrl("jdbc:%s".formatted(url));
		dataSource.setMinPoolSize(2);
		dataSource.setMaxPoolSize(20);
		dataSource.setMaxIdleTime(30000);

		try {
			execute(Extractor.readText("sql/init.sql"));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Connection connect() throws SQLException {
		return dataSource.getConnection();
	}
}
