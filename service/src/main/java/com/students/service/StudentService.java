package com.students.service;

import static com.students.service.validation.ValidationResult.Required;
import static com.students.service.validation.ValidationResult.Valid;
import static com.students.service.validation.Validator.isBoundsCorrect;
import static com.students.service.validation.Validator.isQuerySafe;
import static com.students.service.validation.Validator.validate;
import static com.students.util.Merger.merge;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.students.db.model.Group;
import com.students.db.model.Student;
import com.students.db.sql.StudentRepository;
import com.students.service.result.ListResult;
import com.students.service.result.Result;
import com.students.service.result.SaveResult;
import com.students.service.validation.ValidationResult;

public class StudentService {
	private StudentRepository repo;

	public StudentService(StudentRepository repo) {
		super();
		this.repo = repo;
	}

	public ListResult<Student> list(String query, int offset, int limit) {
		if (!isBoundsCorrect(offset, limit)) {
			return new ListResult<>(true);
		}
		if (query != null && !isQuerySafe(query)) {
			return new ListResult<>(true);
		}
		
		try {
			List<Student> data;
			
			if (query == null) {
				data = repo.list(offset, limit);
			} else {
				data = repo.search(query, offset, limit);
			}
			
			return new ListResult<>(data);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ListResult<>(e.getMessage());
		}
	}
	
	public Result<Group> getStudentGroup(String id) {
		var result = new Result<Group>();
		try {
			var data = repo.getGroup(UUID.fromString(id));
			result.setData(data);
			result.setNotFound(data == null);
		} catch(SQLException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
		}
		return result;		
	}
	
	private Map<String, ValidationResult> validateInstance(Student s) {
		var map = new HashMap<String, ValidationResult>();
		var data = s.getData();
		var contact = s.getContact();
		
		if (data == null) {
			map.put("data", Required);
		}
		if (contact == null) {
			map.put("auth", Required);
			return map;
		}
		
		map.put("first_name", validate(data.getFirstName(), 1, 128, "[a-zA-Zа-яА-Я ]+"));
		map.put("last_name", validate(data.getLastName(), 1, 128, "[a-zA-Zа-яА-Я ]+"));
		if (data.getPatronymic() != null) {
			map.put("patronymic", validate(data.getPatronymic(), 1, 128, "[a-zA-Zа-яА-Я ]+"));
		}
		if (contact.getPhone() != null) {
			map.put("phone", validate(contact.getPhone(), 2, 128, "[+*]?(\\d|[-()_ ])+"));
		}
		if (contact.getEmail() != null) {
			map.put("email", validate(contact.getEmail(), 3, 128, ".+@.+\\..+"));
		}
		
		return map;
	}
	
	private void mergeObjects(Student s, Student old) {
		var newContact = s.getContact();
		var oldContact = old.getContact();
		
		merge(newContact::setPhone, newContact::getPhone, oldContact::getPhone);
		merge(newContact::setEmail, newContact::getEmail, oldContact::getEmail);
		
		var newData = s.getData();
		var oldData = old.getData();
		
		merge(newData::setFirstName, newData::getFirstName, oldData::getFirstName);
		merge(newData::setLastName, newData::getLastName, oldData::getLastName);
		merge(newData::setPatronymic, newData::getPatronymic, oldData::getPatronymic);
	}

	public SaveResult save(Student s) {
		var map = validateInstance(s);
		
		if(map.entrySet().stream().allMatch(t -> t.getValue() == Valid)) {
			try {
				if (s.getId() == null) {
					s.setId(UUID.randomUUID());
					repo.insert(s);
					return new SaveResult(s.getId());
				} else {
					var old = repo.get(s.getId());
					mergeObjects(s, old);
					return new SaveResult(s.getId(), !repo.update(s));
				}
			} catch(SQLException e) {
				e.printStackTrace();
				return new SaveResult(e.getMessage());
			}
		}
		
		return new SaveResult(map);
	}

	public Result<Void> delete(String id) {
		var result = new Result<Void>();
		try {
			result.setNotFound(repo.delete(UUID.fromString(id)));			
		} catch(SQLException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
		}		
		return result;
	}
	
	
}
