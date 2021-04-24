package com.students.data.gen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.students.db.migrations.ExtendedGroup;
import com.students.db.model.Auth;
import com.students.db.model.Contact;
import com.students.db.model.PersonalData;
import com.students.db.model.Student;
import com.students.db.model.Teacher;

public class Generator {

	public Generator() {
		
	}
	
	public List<Teacher> generateTeachers(InputStream in) throws IOException {
		var scanner = new BufferedReader(new InputStreamReader(in));
	
		var result = new ArrayList<Teacher>();
		
		String line;
		while((line = scanner.readLine()) != null) {
			var bio = line.split(" ");
			
			var t = new Teacher();
			t.setId(UUID.randomUUID());
			
			var auth = new Auth();
			auth.setId(UUID.randomUUID());
			
			var login = Randomus.login(bio[1], bio[0]);
			auth.setLogin(login);
			auth.setPassword(login);
			t.setAuth(auth);
			
			var data = new PersonalData();
			data.setId(UUID.randomUUID());
			data.setFirstName(bio[1]);
			data.setLastName(bio[0]);
			data.setPatronymic(bio[2]);
			t.setData(data);
			
			result.add(t);
		}
		
		scanner.close();
		return result;
	}
	
	public List<Student> generateStudents(InputStream in) throws IOException {
		var scanner = new BufferedReader(new InputStreamReader(in));
		
		var result = new ArrayList<Student>();
		
		String line;
		while((line = scanner.readLine()) != null) {
			var bio = line.split(" ");
			
			var s = new Student();
			s.setId(UUID.randomUUID());
			
			var data = new PersonalData();
			data.setId(UUID.randomUUID());
			data.setFirstName(bio[1]);
			data.setLastName(bio[0]);
			data.setPatronymic(bio[2]);
			s.setData(data);
			
			var contact = new Contact();
			contact.setId(UUID.randomUUID());
			contact.setPhone(Randomus.phone());
			
			var login = Randomus.login(bio[1], bio[0]);
			contact.setEmail(Randomus.email(login));
			s.setContact(contact);
			
			result.add(s);
		}
		
		scanner.close();
		return result;
	}

	public List<ExtendedGroup> generateGroups(List<Student> students) throws IOException {
		String[] names = {"21ит18", "21пт13к"};
		
		var result = new ArrayList<ExtendedGroup>();
		
		for (int i = 0; i < names.length; i++) {
			var g = new ExtendedGroup();
			
			g.setId(UUID.randomUUID());
			g.setCode(names[i]);
			g.setDuration(8);
			
			var ids = new UUID[25];
			for (int j = 0; j < 25; j++) {
				if (i * 25 + j > students.size()) {
					break;
				}
				
				ids[j] = students.get(i * 25 + j).getId();
			}
			
			g.setStudents(ids);
			result.add(g);
		}
		
		return result;
	}
}
