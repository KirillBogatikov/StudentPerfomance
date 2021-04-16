package com.students.service;

import static com.students.service.validation.ValidationResult.Required;
import static com.students.service.validation.Validator.validate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.students.db.model.Teacher;
import com.students.db.sql.TeacherRepository;

public class TeacherService {
	private TeacherRepository repo;

	public TeacherService(TeacherRepository repo) {
		super();
		this.repo = repo;
	}

	public ListResult<Teacher> list(int offset, int limit) {
		var result = new ListResult<Teacher>();
		
		if (offset < 0 || limit < 1 || limit > 1000) {
			result.setBoundsIncorrect(true);
			return result;
		}
		
		try {
			result.setData(repo.list(offset, limit));
		} catch (SQLException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
		}
		
		return result;
	}

	public ListResult<Teacher> search(String query, int offset, int limit) {
		var result = new ListResult<Teacher>();
		
		if (offset < 0 || limit < 1 || limit > 1000) {
			result.setBoundsIncorrect(true);
			return result;
		}
		
		if (query.toLowerCase().matches("select|drop|create|table|insert|delete|update|truncate")) {
			result.setQueryIncorrect(true);
			return result;
		}
		
		try {
			result.setData(repo.search(query, offset, limit));
		} catch (SQLException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
		}
		
		return result;
	}
	
	private Map<String, Object> validateInstance(Teacher t) {
		var map = new HashMap<String, Object>();
		var auth = t.getAuth();
		var data = t.getData();
		
		if (auth == null) {
			map.put("auth", Required);
			return map;
		}
		
		if (data == null) {
			map.put("data", Required);
		}
		
		map.put("login", validate(auth.getLogin(), 4, 16, "\\w+"));
		map.put("password", validate(auth.getPassword(), 8, 128, ".+"));
		map.put("first_name", validate(data.getFirstName(), 1, 128, "[a-zA-Zа-яА-Я ]+"));
		map.put("last_name", validate(data.getLastName(), 1, 128, "[a-zA-Zа-яА-Я ]+"));
		if (data.getPatronymic() != null) {
			map.put("patronymic", validate(data.getPatronymic(), 1, 128, "[a-zA-Zа-яА-Я ]+"));
		}
		
		return map;
	}
	
	public Result<Map<String, Object>> save(Teacher t) {
		var result = new Result<Map<String, Object>>();
		var map = validateInstance(t);
		if(map.isEmpty()) {
			try {
				repo.save(t);
			} catch(SQLException e) {
				e.printStackTrace();
				result.setError(e.getMessage());
			}
		}
		result.setData(map);
		
		return result;
	}
	
	public Result<Boolean> delete(UUID id) {
		try {
			repo.delete(id);
			return new Result<>(true);
		} catch (SQLException e) {
			e.printStackTrace();
			var r = new Result<>(false);
			r.setError(e.getMessage());
			return r;
		}
	}
}
