package com.students.db.migrations.api;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.students.db.migrations.ExtendedGroup;
import com.students.db.model.Auth;
import com.students.db.model.Contact;
import com.students.db.model.Discipline;
import com.students.db.model.Mark;
import com.students.db.model.PersonalData;
import com.students.db.model.Student;
import com.students.db.model.Teacher;

public class Api {
	private static final String API_URL = "https://randomuser.me/api/";
	private Random random = new Random();

	private List<User> loadJson(int count, String params) throws IOException {
		var url = "%s?results=%d&%s".formatted(API_URL, count, params);
		try (var stream = new URL(url).openStream()) {
			var mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			var data = mapper.readValue(stream, Result.class);
			return data.getResults();
		}
	}

	private final String alphabet = "абвгдежзиклмнопрстуфхцчшэюя";

	private char randomChar() {
		return alphabet.charAt(random.nextInt(alphabet.length()));
	}

	private PersonalData of(Name name) {
		var d = new PersonalData();
		d.setId(UUID.randomUUID());
		d.setFirstName(name.getFirst());
		d.setLastName(name.getLast());
		return d;
	}

	private Auth of(Login login) {
		var a = new Auth();
		a.setId(UUID.randomUUID());
		a.setLogin(login.getUsername());
		a.setPassword(login.getPassword());
		return a;
	}

	public List<Teacher> generateTeachers(int count) throws IOException {
		var models = loadJson(count, "inc=name,login");
		var result = new ArrayList<Teacher>();

		for (var m : models) {
			var t = new Teacher();

			t.setId(UUID.randomUUID());
			t.setAuth(of(m.getLogin()));
			t.setData(of(m.getName()));

			result.add(t);
		}
		
		var def = new Teacher();
		def.setId(UUID.randomUUID());
		var a = new Auth();
		a.setId(UUID.randomUUID());
		a.setLogin("test");
		a.setPassword("test");
		def.setAuth(a);
		var d = new PersonalData();
		d.setId(UUID.randomUUID());
		d.setFirstName("Антон");
		d.setLastName("Лапенко");
		d.setPatronymic("Владимирович");
		def.setData(d);
		
		result.add(def);

		return result;
	}

	public List<Student> generateStudents(int count) throws IOException {
		var models = loadJson(count, "inc=name,login,phone,email");
		var result = new ArrayList<Student>();

		for (var m : models) {
			var s = new Student();

			s.setId(UUID.randomUUID());
			s.setData(of(m.getName()));
			var c = new Contact();
			c.setId(UUID.randomUUID());
			c.setPhone(m.getPhone());
			c.setEmail(m.getEmail());
			s.setContact(c);

			result.add(s);
		}

		return result;
	}

	public List<ExtendedGroup> generateGroups(int maxPerGroup, List<Student> students) throws IOException {
		var result = new ArrayList<ExtendedGroup>();

		int index = 0;
		int groupCount = students.size() / maxPerGroup;
		for (var i = 0; i < groupCount; i++) {
			var group = new ExtendedGroup();
			group.setId(UUID.randomUUID());
			group.setCode("%d%s%s%s%d".formatted(random.nextInt(100), randomChar(), randomChar(), randomChar(),
					random.nextInt(100)));
			group.setDuration(random.nextDouble() > 0.7 ? 8 : 10);

			var studentsCount = random.nextInt(8) + (maxPerGroup - 8);
			var groupStudents = new UUID[studentsCount];
			for (var j = 0; j < studentsCount; j++, index++) {
				groupStudents[j] = students.get(index).getId();
			}

			group.setStudents(groupStudents);
			result.add(group);
		}

		return result;
	}

	private static final List<String> disciplines = Arrays.asList("Русский язык", "Литература", "Иностранный язык (английский)",
			"Алгебра", "Геометрия", "Высшая математика", "Информатика и ИКТ", "История", "Обществознание", "Право", "Экономика", "География", "Физика",
			"Химия", "Биология", "Физическая культура", "Экология", "Теория вероятностей и математическая статистика");

	public List<Discipline> generateDisciplines(List<Teacher> teachers) {
		var result = new ArrayList<Discipline>();
		
		for (int i = 0; i < disciplines.size(); i++) {
			var discipline = new Discipline();
			
			discipline.setId(UUID.randomUUID());
			discipline.setName(disciplines.get(i));
			discipline.setTeacher(teachers.get(i));
			
			result.add(discipline);
		}
		
		return result;
	}
	
	public List<Mark> generateMarks(List<Discipline> disciplines, List<ExtendedGroup> groups) {
		var result = new ArrayList<Mark>();

		for (var group : groups) {
			
			for (var d : disciplines) {
				for (var studentId : group.getStudents()) {
					var mark = new Mark();
					
					mark.setId(UUID.randomUUID());
					mark.setDiscipline(d);
					mark.setMark(random.nextInt(3) + 3);
					
					var student = new Student();
					student.setId(studentId);
					mark.setStudent(student);
					
					result.add(mark);	
				}	
			}
			
		}
		
		return result;
	}
}
