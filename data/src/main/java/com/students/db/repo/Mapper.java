package com.students.db.repo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.students.func.UnsafeValueProcessor;

public interface Mapper<T> extends UnsafeValueProcessor<ResultSet, T, SQLException> {
	public default T defaultValue() {
		return null;
	}
}
