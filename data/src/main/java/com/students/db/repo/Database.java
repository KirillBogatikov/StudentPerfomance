package com.students.db.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.students.db.sql.Mapping;

public abstract class Database {
	public abstract Connection connect() throws SQLException;
	
	public void fillStatement(PreparedStatement stat, Object...args) throws SQLException {
		for (int i = 0; i < args.length; i++) {
			stat.setObject(i + 1, args[i]);
		}
	}
	
	public <T> T query(Mapper<T> mapper, String sql, Object... args) throws SQLException {
		try(var conn = connect();
			var stat = conn.prepareStatement(sql)) {
			fillStatement(stat, args);
			ResultSet result = stat.executeQuery();
			
			if(result.next()) {
				return mapper.process(result);
			}
			
			return mapper.defaultValue();
		}
	}
	
	public <T> T query(Class<T> type, String sql, Object... args) throws SQLException {
		return query(Mapping.getInstance().forType(type), sql, args);
	}
	
	public <T> List<T> queryList(Class<T> type, String sql, Object... args) throws SQLException {
		return query(Mapping.getInstance().forList(type), sql, args);
	}
	
	public boolean execute(String sql, Object... args) throws SQLException {
		try(var conn = connect();
			var stat = conn.prepareStatement(sql)) {
			fillStatement(stat, args);
			return stat.execute();
		}
	}
}
