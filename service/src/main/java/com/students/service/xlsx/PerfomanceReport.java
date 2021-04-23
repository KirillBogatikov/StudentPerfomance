package com.students.service.xlsx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.students.db.model.Discipline;
import com.students.db.model.Mark;
import com.students.db.model.PersonalData;
import com.students.db.model.Student;

public class PerfomanceReport extends XlsxReport {
	private List<Mark> marks;
	private List<Student> students;
	private List<Discipline> disciplines;
	private String code;
	
	public PerfomanceReport(String code, List<Mark> marks, List<Student> students, List<Discipline> disciplines) {
		super("Ведомость успеваемости учащихся");
		this.code = code;
		this.marks = marks;
		this.students = students;
		this.disciplines = disciplines;
		
		for (var i = 0; i < disciplines.size(); ) {
			var d = this.disciplines.get(i);
			boolean found = false;
			
			for (var j = 0; j < marks.size(); j++) {
				if (marks.get(j).getDiscipline().getId().equals(d.getId())) {
					found = true;
					break;
				}
			}
			
			if (found) {
				i++;
			} else {
				this.disciplines.remove(i);
			}
		}
	}
	
	private Map<Student, List<Mark>> map() {
		var map = new HashMap<Student, List<Mark>>();
		
		for (var i = 0; i < marks.size(); i++) {
			var mark = marks.get(i);
			var list = map.get(mark.getStudent());
			if (list == null) {
				list = new ArrayList<Mark>();
				map.put(mark.getStudent(), list);
			}
			
			list.add(mark);
		}
		
		return map;
	}
	
	private String formatName(PersonalData data) {
		var patronymic = data.getPatronymic();
		return "%s %s %s".formatted(data.getLastName(), data.getFirstName(), patronymic == null ? "" : patronymic);
	}
	
	@Override
	public void export(Sheet sheet) {
		var row = sheet.createRow(0);
		var titleCell = row.createCell(0);
		titleCell.setCellValue("Ведомость успеваемости учащихся");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, disciplines.size() + 2));
		
		row = sheet.createRow(2);
		var groupTitleCell = row.createCell(2);
		groupTitleCell.setCellValue("Группа: " + code);
		
		row = sheet.createRow(4);
		var headerNoCell = row.createCell(0);
		headerNoCell.setCellValue("№ п/п");
		
		var headerBio = row.createCell(1);
		headerBio.setCellValue("ФИО студента");
		
		for (var i = 0; i < disciplines.size(); i++) {
			var cell = row.createCell(2 + i);
			var d = disciplines.get(i);
			cell.setCellValue(d.getName() + " (" + formatName(d.getTeacher().getData()) + ")");
		}
		
		var mapped = map();
		
		for (var i = 0; i < students.size(); i++) {
			var student = students.get(i);
			row = sheet.createRow(5 + i);
			var cell = row.createCell(0);
			cell.setCellValue(i + 1);
			
			cell = row.createCell(1);
			cell.setCellValue(formatName(student.getData()));
			
			for (var j = 0; j < disciplines.size(); j++) {
				var discipline = disciplines.get(j);
				var opt = mapped.get(student)
					.stream()
					.filter(m -> 
						m.getDiscipline()
							.getId().equals(discipline.getId()))
					.findFirst();
				
				if (opt.isPresent()) {
					cell = row.createCell(2 + j);
					cell.setCellValue(opt.get().getMark());
				}
			}
			
		}
	}

}
