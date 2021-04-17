package com.students.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Group;
import com.students.db.model.Student;
import com.students.db.sql.GroupRepository;
import com.students.service.result.ListResult;
import com.students.service.result.SaveResult;
import com.students.service.validation.ValidationResult;

public class GroupService {
	private GroupRepository repo;
	
	public GroupService(GroupRepository repo) {
		this.repo = repo;
	}

	public ListResult<Group> list(String query, int offset, int limit) {
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

	public String delete(String id) {
		try {
			repo.delete(UUID.fromString(id));
			return null;
		} catch(SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
