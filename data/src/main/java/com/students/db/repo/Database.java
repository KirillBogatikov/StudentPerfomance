package com.students.db.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.students.db.sql.Mapping;

public abstract class Database {
	public abstract Connection connect() throws SQLException;
	
	public PreparedStatement prepareStatement(String sql, Object...args) throws SQLException {
		var stat = connect().prepareStatement(sql);
		for (int i = 0; i < args.length; i++) {
			stat.setObject(i + 1, args[i]);
		}
		return stat;
	}
	
	public <T> T query(Mapper<T> mapper, String sql, Object... args) throws SQLException {
		try(var stat = prepareStatement(sql, args)) {			
			ResultSet result = stat.executeQuery();
			
			if(result.next()) {
				return mapper.process(result);
			}
			
			return null;
		}
	}
	
	public <T> T query(Class<T> type, String sql, Object... args) throws SQLException {
		return query(Mapping.getInstance().forType(type), sql, args);
	}
	
	public <T> List<T> queryList(Class<T> type, String sql, Object... args) throws SQLException {
		return query(Mapping.getInstance().forList(type), sql, args);
	}
	
	public boolean execute(String sql, Object... args) throws SQLException {
		try (var stat = prepareStatement(sql, args)) {
			return stat.execute();
		}
	}
}
