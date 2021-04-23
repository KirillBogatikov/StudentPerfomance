package com.students.db.sql;

import static com.students.util.Extractor.readText;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Group;
import com.students.db.model.Student;
import com.students.db.repo.Database;

public class GroupRepository extends SqlRepository {
	private static final String DIR = "sql/group";
	private static final String has = readText(DIR, "has.sql"), list = readText(DIR, "list.sql"),
			search = readText(DIR, "search.sql"),
			listStudents = readText(DIR, "list_students.sql"), insert = readText(DIR, "insert.sql"),
			update = readText(DIR, "update.sql"), delete = readText(DIR, "delete.sql"),
			move = readText(DIR, "move.sql"),
			checkMove = readText(DIR, "check_move.sql"), remove = readText(DIR, "remove.sql"),
			byStudent = readText(DIR, "by_student.sql");

	public GroupRepository(Database database) {
		super(database);
		mapping.register(Group.class, r -> {
			var g = new Group();

			g.setId(r.getObject("group_id", UUID.class));
			g.setDuration(r.getInt("group_duration"));
			g.setCode(r.getString("group_code"));

			return g;
		});
	}
	
	public String code(UUID id) throws SQLException {
		return database.query(r -> r.getString("group_code"), "SELECT g.code as group_code FROM \"group\" AS g WHERE id = ?", id);
	}
	
	public Group getStudentGroup(UUID student) throws SQLException {
		return database.query(Group.class, byStudent, student);
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

	public boolean moveStudent(UUID newId, UUID student, UUID newGroup) throws SQLException {
		if (database.query(r -> 1, checkMove, newGroup, student) == null) {
			return false;
		}

		database.execute(remove, student);
		database.execute(move, newId, newGroup, student);
		return true;
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

	public boolean delete(UUID id) throws SQLException {
		if (!has(id)) {
			return false;
		}

		database.execute(delete, id);
		return true;
	}

	public boolean has(UUID id) throws SQLException {
		return database.query(r -> 1, has, id) != null;
	}

	public boolean removeStudent(UUID oldGroup, UUID student) throws SQLException {
		if (database.query(r -> 1, checkMove, oldGroup, student) == null) {
			return false;
		}

		database.execute(remove, student);
		return true;
	}
}
