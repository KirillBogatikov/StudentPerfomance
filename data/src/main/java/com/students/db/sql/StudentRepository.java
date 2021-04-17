package com.students.db.sql;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Contact;
import com.students.db.model.PersonalData;
import com.students.db.model.Student;
import com.students.db.repo.Database;
import com.students.util.Extractor;

public class StudentRepository extends SqlRepository {
	private static final String DIR = "/sql/student";
	private static final String list = Extractor.readText(DIR, "list.sql"),
								search = Extractor.readText(DIR, "search.sql");

	public StudentRepository(Database database) {
		super(database);
		mapping.register(Contact.class, r -> {
			var c = new Contact();
			
			c.setId(r.getObject("contact_id", UUID.class));
			c.setPhone(r.getString("contact_phone"));
			c.setEmail(r.getString("contact_email"));
			
			return c;
		});
		mapping.register(Student.class, r -> {
			var s = new Student();
			
			s.setId(r.getObject("student_id", UUID.class));
			s.setData(mapping.forType(PersonalData.class).process(r));
			s.setContact(mapping.forType(Contact.class).process(r));
			
			return s;
		});
	}

	public List<Student> list(int offset, int limit) throws SQLException {
		return database.queryList(Student.class, list, offset, limit);
	}
	
	public List<Student> search(String query, int offset, int limit) throws SQLException {
		return database.queryList(Student.class, search, query, offset, limit);
	}
}
