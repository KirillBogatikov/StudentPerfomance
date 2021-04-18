package com.students.service;

import static com.students.service.validation.Validator.isBoundsCorrect;
import static com.students.service.validation.Validator.isQuerySafe;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Discipline;
import com.students.db.sql.DisciplineRepository;
import com.students.service.result.ListResult;
import com.students.service.result.Result;
import com.students.service.result.SaveResult;

public class DisciplineService {
	private DisciplineRepository repo;
	
	public DisciplineService(DisciplineRepository repo) {
		this.repo = repo;
	}
	
	public ListResult<Discipline> list(String query, int offset, int limit) {
		if (!isBoundsCorrect(offset, limit)) {
			return new ListResult<>(true);
		}
		if (query != null && !isQuerySafe(query)) {
			return new ListResult<>(true);
		}
		
		List<Discipline> data;
		
		try {
			if (query == null) {
				data = repo.list(offset, limit);
			} else {
				data = repo.search(query, offset, limit);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return new ListResult<>(e.getMessage());
		}
		
		return new ListResult<>(data);
	}

	public SaveResult save(Discipline d) {
		try {
			
			if (d.getId() == null) {
				d.setId(UUID.randomUUID());
				repo.insert(d);
				return new SaveResult(d.getId());
			} else {
				var u = repo.update(d);
				return new SaveResult(d.getId(), u);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return new SaveResult(e.getMessage());
		}
	}

	public Result<Void> delete(String id) {
		var r = new Result<Void>();
		try {
			r.setNotFound(repo.delete(UUID.fromString(id)));
		} catch(SQLException e) {
			e.printStackTrace();
			r.setError(e.getMessage());
		}
		return r;
	}
}
