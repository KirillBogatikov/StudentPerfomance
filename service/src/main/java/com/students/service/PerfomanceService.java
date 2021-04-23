package com.students.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import com.students.db.sql.GroupRepository;
import com.students.db.sql.MarkRepository;
import com.students.db.sql.StudentRepository;
import com.students.service.result.SaveResult;
import com.students.service.xlsx.PerfomanceReport;

public class PerfomanceService {
	private MarkRepository repo;
	private GroupRepository groups;
	private StudentRepository students;
	
	public PerfomanceService(MarkRepository repo, GroupRepository groups, StudentRepository students) {
		this.repo = repo;
		this.groups = groups;
		this.students = students;
	}

	public SaveResult exportGroup(String id) {
		var group = UUID.fromString(id);
		try {
			var code = groups.code(group);
			if (code == null) {
				return new SaveResult(null, true);
			}
			
			var disciplines = repo.listDisciplines();
			var students = groups.listStudents(group);
			var marks = repo.listGroupMarks(group);
			
			var report = new PerfomanceReport(code, marks, students, disciplines);
			return new SaveResult(report.write("reports/xlsx/group"));
		} catch(SQLException | IOException e) {
			e.printStackTrace();
			return new SaveResult(e.getMessage());
		}
	}

	public SaveResult exportStudent(String id) {
		var studentId = UUID.fromString(id);
		try {
			var student = students.get(studentId);
			if (student == null) {
				return new SaveResult(null, true);
			}
			
			var group = groups.getStudentGroup(studentId);
			var disciplines = repo.listDisciplines();
			var marks = repo.listGroupMarks(group.getId());
			
			var report = new PerfomanceReport(group.getCode(), marks, Arrays.asList(student), disciplines);
			return new SaveResult(report.write("reports/xlsx/group"));
		} catch(SQLException | IOException e) {
			e.printStackTrace();
			return new SaveResult(e.getMessage());
		}
	}
}
