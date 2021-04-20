package com.students.db.sql;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Contact;
import com.students.db.model.Group;
import com.students.db.model.PersonalData;
import com.students.db.model.Student;
import com.students.db.repo.Database;
import static com.students.util.Extractor.readText;

public class StudentRepository extends SqlRepository {
	private static final String DIR = "sql/student";
	private static final String get = readText(DIR, "get.sql"),
			getGroup = readText(DIR, "get_group.sql"), 
			has = readText(DIR, "has.sql"), 
			list = readText(DIR, "list.sql"),
			search = readText(DIR, "search.sql"), 
			insert = readText(DIR, "insert.sql"),
			update = readText(DIR, "update.sql"), 
			delete = readText(DIR, "delete.sql");

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
	
	public boolean has(UUID id) throws SQLException {
		return database.query(r -> 1, has, id) != null;
	}

	public List<Student> list(int offset, int limit) throws SQLException {
		return database.queryList(Student.class, list, offset, limit);
	}

	public List<Student> search(String query, int offset, int limit) throws SQLException {
		return database.queryList(Student.class, search, query, query, query, query, query, offset, limit);
	}

	public void insert(Student s) throws SQLException {
		var data = s.getData();
		var contact = s.getContact();
		database.execute(insert, data.getId(), data.getFirstName(), data.getLastName(), data.getPatronymic(),
				contact.getId(), contact.getPhone(), contact.getEmail(), 
				s.getId(), data.getId(), contact.getId());
	}

	public boolean update(Student s) throws SQLException {
		if (!has(s.getId())) {
			return false;
		}
		
		var data = s.getData();
		var contact = s.getContact();
		database.execute(update, data.getFirstName(), data.getLastName(), data.getPatronymic(), data.getId(), 
			contact.getPhone(), contact.getEmail(), contact.getId());
		return true;
	}
	
	public boolean delete(UUID id) throws SQLException {
		if (!has(id)) {
			return false;
		}
		
		database.execute(delete, id);
		return true;
	}

	public Student get(UUID id) throws SQLException {
		return database.query(Student.class, get, id);
	}
	
	public Group getGroup(UUID student) throws SQLException {
		return database.query(Group.class, getGroup, student);
	}
}
