package com.students.service;

import static com.students.service.validation.ValidationResult.Required;
import static com.students.service.validation.Validator.isBoundsCorrect;
import static com.students.service.validation.Validator.isQuerySafe;
import static com.students.service.validation.Validator.validate;
import static com.students.util.Merger.merge;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.students.db.model.Teacher;
import com.students.db.sql.TeacherRepository;
import com.students.service.result.ListResult;
import com.students.service.result.Result;
import com.students.service.result.SaveResult;
import com.students.service.validation.ValidationResult;
import com.students.util.Hash;

public class TeacherService {
	private TeacherRepository repo;
	private String salt;
	private int saltPosition;

	public TeacherService(TeacherRepository repo, String salt, int saltPosition) {
		super();
		this.repo = repo;
		this.salt = salt;
		this.saltPosition = saltPosition;
	}

	public ListResult<Teacher> list(String query, int offset, int limit) {
		if (!isBoundsCorrect(offset, limit)) {
			return new ListResult<>(true);
		}
		if (!isQuerySafe(query)) {
			return new ListResult<>(true);
		}
		
		try {
			return new ListResult<>(repo.list(offset, limit));
		} catch (SQLException e) {
			e.printStackTrace();
			return new ListResult<>(e.getMessage());
		}
	}
	
	private Map<String, ValidationResult> validateInstance(Teacher t) {
		var map = new HashMap<String, ValidationResult>();
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
	
	private void mergeObjects(Teacher t, Teacher old) {
		var newAuth = t.getAuth();
		var oldAuth = old.getAuth();
		
		merge(newAuth::setLogin, newAuth::getLogin, oldAuth::getLogin);
		merge(newAuth::setPasswordHash, newAuth::getPasswordHash, oldAuth::getPasswordHash);
		
		var newData = t.getData();
		var oldData = old.getData();
		
		merge(newData::setFirstName, newData::getFirstName, oldData::getFirstName);
		merge(newData::setLastName, newData::getLastName, oldData::getLastName);
		merge(newData::setPatronymic, newData::getPatronymic, oldData::getPatronymic);
	}
	
	public SaveResult save(Teacher t) {
		var map = validateInstance(t);
		
		if(map.isEmpty()) {
			try {
				var auth = t.getAuth();
				if (auth.getPassword() != null) {
					auth.setPasswordHash(Hash.hash(auth.getPassword(), salt, saltPosition));
				}
				
				if (t.getId() == null) {
					repo.insert(t);
					return new SaveResult(t.getId());
				} else {
					var old = repo.get(t.getId());
					mergeObjects(t, old);					
					var u = repo.update(t);
					return new SaveResult(t.getId(), u);
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
				return new SaveResult(e.getMessage());
			}
		}
		
		return new SaveResult(map);
	}
	
	public Result<Void> delete(UUID id) {
		var result = new Result<Void>();
		try {
			result.setNotFound(repo.delete(id));
		} catch (SQLException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
		}
		return result;
	}

	public Result<Teacher> getByAuth(UUID authId) {
		var result = new Result<Teacher>();
		try {
			var teacher = repo.getByAuthId(authId);
			result.setData(teacher);
			result.setNotFound(teacher == null);
		} catch(SQLException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
		}
		return result;
	}
}
