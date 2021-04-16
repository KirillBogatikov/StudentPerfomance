package com.students.db.sql;

import com.students.db.repo.Database;

public abstract class SqlRepository {
	protected Database database;
	protected final Mapping mapping = Mapping.getInstance();
	
	public SqlRepository(Database database) {
		this.database = database;
	}
}
