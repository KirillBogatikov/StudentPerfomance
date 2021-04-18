package com.students.service;

import static com.students.service.validation.Validator.isBoundsCorrect;
import static com.students.service.validation.Validator.isQuerySafe;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Group;
import com.students.db.model.Student;
import com.students.db.sql.GroupRepository;
import com.students.service.result.ListResult;
import com.students.service.result.Result;
import com.students.service.result.SaveResult;
import com.students.service.validation.ValidationResult;

public class GroupService {
	private GroupRepository repo;
	
	public GroupService(GroupRepository repo) {
		this.repo = repo;
	}

	public ListResult<Group> list(String query, int offset, int limit) {
		if (!isBoundsCorrect(offset, limit)) {
			return new ListResult<>(true);
		}
		if (query != null && !isQuerySafe(query)) {
			return new ListResult<>(true);
		}
		
		try {
			List<Group> data;
			if (query == null) {
				data = repo.list(offset, limit);
			} else {
				data = repo.search(query, offset, limit);
			}
			return new ListResult<>(data);
		} catch(SQLException e) {
			e.printStackTrace();
			return new ListResult<>(e.getMessage());
		}
	}

	public ListResult<Student> listStudents(String id) {
		try {
			var uid = UUID.fromString(id);
			if (!repo.has(uid)) {
				return new ListResult<>();
			}
			
			List<Student> list = repo.listStudents(uid);
			return new ListResult<>(list);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ListResult<>(e.getMessage());
		}
	}

	public SaveResult save(Group g) {
		if (g.getCode() == null || g.getCode().trim().isEmpty()) {
			var map = new HashMap<String, ValidationResult>();
			map.put("code", ValidationResult.Required);
			return new SaveResult(map);
		}
		
		try {
			if (g.getId() == null) {
				g.setId(UUID.randomUUID());
				repo.insert(g);
				return new SaveResult(g.getId());
			} else {
				var u = repo.update(g);
				return new SaveResult(g.getId(), u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new SaveResult(e.getMessage());
		}
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

	public Result<Void> moveStudent(String newGroup, String student) {
		var result = new Result<Void>();
		
		try {
			var m = repo.moveStudent(UUID.randomUUID(), UUID.fromString(student), UUID.fromString(newGroup));
			result.setNotFound(m);
		} catch(SQLException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
		}
		
		return result;
	}

}
