package com.students.service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Discipline;
import com.students.db.model.Mark;
import com.students.db.sql.MarkRepository;
import com.students.service.result.ListResult;
import com.students.service.result.SaveResult;

public class MarkService {
	private MarkRepository repo;
	
	public MarkService(MarkRepository repo) {
		this.repo = repo;
	}
	
	private List<Mark> filter(List<Mark> origin, List<String> disciplines) {
		if (disciplines == null || disciplines.isEmpty()) {
			return origin;
		}
		
		for (var i = 0; i < origin.size(); ) {
			var id = origin.get(i).getDiscipline().getId().toString();
			
			if (disciplines.contains(id)) {
				i++;
			} else {
				origin.remove(i);
			}
		}
		return origin;
	}

	public ListResult<Mark> listStudentMarks(String studentId, List<String> disciplines) {
		try {
			return new ListResult<>(filter(repo.listStudentMarks(UUID.fromString(studentId)), disciplines));
		} catch(SQLException e) {
			e.printStackTrace();
			return new ListResult<>(e.getMessage());
		}
	}

	public ListResult<Mark> listGroupMarks(String groupId, List<String> disciplines) {
		try {
			return new ListResult<>(filter(repo.listGroupMarks(UUID.fromString(groupId)), disciplines));
		} catch(SQLException e) {
			e.printStackTrace();
			return new ListResult<>(e.getMessage());
		}
	}

	public SaveResult saveMark(Mark mark) {
		try {
			var found = false;
			if (mark.getId() == null) {
				mark.setId(UUID.randomUUID());
				found = repo.insertMark(mark);
				
			} else {
				found = repo.updateMark(mark);
			}
			
			return new SaveResult(mark.getId(), !found);
		} catch(SQLException e) {
			e.printStackTrace();
			return new SaveResult(e.getMessage());
		}
	}

	public ListResult<Discipline> listDisciplines() {
		try {
			return new ListResult<>(repo.listDisciplines());
		} catch(SQLException e) {
			e.printStackTrace();
			return new ListResult<>(e.getMessage());
		}
	}

	public SaveResult addDiscipline(Discipline discipline) {
		try {
			discipline.setId(UUID.randomUUID());
			var found = repo.insertDiscipline(discipline);
			return new SaveResult(discipline.getId(), !found);
		} catch(SQLException e) {
			e.printStackTrace();
			return new SaveResult(e.getMessage());
		}
	}
}
