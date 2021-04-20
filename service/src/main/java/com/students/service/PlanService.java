package com.students.service;

import java.sql.SQLException;
import java.util.UUID;

import com.students.db.model.Discipline;
import com.students.db.model.Plan;
import com.students.db.sql.PlanRepository;
import com.students.service.result.Result;
import com.students.service.result.SaveResult;

public class PlanService {
	private PlanRepository repo;
	
	public PlanService(PlanRepository repo) {
		this.repo = repo;
	}
	
	public SaveResult save(Plan p) {
		try {
			p.setId(UUID.randomUUID());
			repo.insert(p);
			
			if (p.getDisciplines() != null) {
				for (var d : p.getDisciplines()) {
					repo.insertDiscipline(p.getId(), d);
				}
			}
			
			return new SaveResult(p.getId());
		} catch(SQLException e) {
			e.printStackTrace();
			return new SaveResult(e.getMessage());
		}
	}

	public SaveResult saveDiscipline(String planId, Discipline discipline) {
		try {
			if (discipline.getId() == null) {
				var u = repo.updateDiscipline(discipline);
				return new SaveResult(discipline.getId(), !u);
			} else {
				discipline.setId(UUID.randomUUID());
				var u = repo.insertDiscipline(UUID.fromString(planId), discipline);
				return new SaveResult(discipline.getId(), !u);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return new SaveResult(e.getMessage());
		}
	}

	public Result<Void> deleteDiscipline(String planId, String disciplineId) {
		var r = new Result<Void>();
		try {
			r.setNotFound(repo.deleteDiscipline(UUID.fromString(planId), UUID.fromString(disciplineId)));
		} catch(SQLException e) {
			e.printStackTrace();
			r.setError(e.getMessage());
		}
		return r;
	}

	public Result<Void> delete(String id) {
		var r = new Result<Void>();
		try {
			r.setNotFound(!repo.delete(UUID.fromString(id)));
		} catch(SQLException e) {
			e.printStackTrace();
			r.setError(e.getMessage());
		}
		return r;
	}
}
