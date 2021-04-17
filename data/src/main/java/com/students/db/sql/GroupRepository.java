package com.students.db.sql;

import static com.students.util.Extractor.readText;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Discipline;
import com.students.db.model.Group;
import com.students.db.model.Mark;
import com.students.db.model.Plan;
import com.students.db.model.Student;
import com.students.db.repo.Database;

public class GroupRepository extends SqlRepository {
	private static final String DIR = "group";
	private static final String has = readText(DIR, "has.sql"), list = readText(DIR, "list.sql"),
			search = readText(DIR, "search.sql"), listStudents = readText(DIR, "listStudents.sql"),
			insert = readText(DIR, "insert.sql"), update = readText(DIR, "update.sql"),
			delete = readText(DIR, "delete.sql"), perfomance = readText(DIR, "perfomance.sql");

	public GroupRepository(Database database) {
		super(database);
		mapping.register(Group.class, r -> {
			var g = new Group();

			g.setId(r.getObject("group_id", UUID.class));
			g.setDuration(r.getInt("group_duration"));
			g.setCode(r.getString("group_code"));
			g.setCount(r.getInt("group_count"));

			return g;
		});
		mapping.register(Mark.class, r -> {
			var m = new Mark();
			
			m.setId(r.getObject("mark_id", UUID.class));
			m.setDiscipline(mapping.forType(Discipline.class).process(r));
			m.setStudent(mapping.forType(Student.class).process(r));
			m.setMark(r.getInt("mark_mark"));
			m.setTime(r.getTimestamp("mark_date"));
			
			return m;
		});
	}

	public List<Group> list(int offset, int limit) throws SQLException {
		return database.queryList(Group.class, list, offset, limit);
	}

	public List<Group> search(String query, int offset, int limit) throws SQLException {
		return database.queryList(Group.class, search, query, offset, limit);
	}

	public List<Student> listStudents(UUID group) throws SQLException {
		return database.queryList(Student.class, listStudents, group);
	}

	public void insert(Group g) throws SQLException {
		database.execute(insert, g.getId(), g.getCode(), g.getDuration());
	}

	public boolean update(Group g) throws SQLException {
		if (database.query(r -> 1, has, g.getId()) == null) {
			return false;
		}
		
		database.execute(update, g.getCode(), g.getDuration(), g.getId());
		return true;
	}

	public void delete(UUID id) throws SQLException {
		database.execute(delete, id);
	}
	
	public boolean has(UUID id) throws SQLException {
		return database.query(r -> 1, has, id) != null;
	}
	
	public List<Mark> perfomance(UUID id, int semester, List<UUID> disciplines) throws SQLException {
		return database.queryList(Mark.class, perfomance, id, semester, disciplines);
	}
}
