package com.students.db.sql;

import static com.students.util.Extractor.readText;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Discipline;
import com.students.db.model.Mark;
import com.students.db.model.Student;
import com.students.db.model.Teacher;
import com.students.db.repo.Database;

public class MarkRepository extends SqlRepository {
	private static final String MARK_DIR = "sql/mark", DISC_DIR = "sql/discipline";
	private static final String has = readText(MARK_DIR, "has.sql"),
			hasDiscipline = readText(MARK_DIR, "has_discipline.sql"),
			hasTeacher = readText(MARK_DIR, "has_teacher.sql"), hasStudent = readText(MARK_DIR, "has_student.sql"),
			getStudentMarks = readText(MARK_DIR, "by_student.sql"), getGroupMarks = readText(MARK_DIR, "by_group.sql"),
			insert = readText(MARK_DIR, "insert.sql"), update = readText(MARK_DIR, "update.sql"),
			insertDiscipline = readText(DISC_DIR, "insert.sql"), listDisciplines = readText(DISC_DIR, "list.sql");

	private StudentRepository studentRepository;

	public MarkRepository(Database database) {
		super(database);

		studentRepository = new StudentRepository(database);

		mapping.register(Discipline.class, r -> {
			var d = new Discipline();

			d.setId(r.getObject("discipline_id", UUID.class));
			d.setName(r.getString("discipline_name"));
			d.setTeacher(mapping.forType(Teacher.class).process(r));

			return d;
		});
		mapping.register(Mark.class, r -> {
			var m = new Mark();

			m.setId(r.getObject("mark_id", UUID.class));
			m.setMark(r.getInt("mark_mark"));
			m.setDiscipline(mapping.forType(Discipline.class).process(r));
			var s = new Student();
			s.setId(r.getObject("student_id", UUID.class));
			m.setStudent(s);

			return m;
		});
	}

	public boolean has(UUID id) throws SQLException {
		return database.query(r -> 1, has, id) != null;
	}

	public boolean hasDiscipline(UUID id) throws SQLException {
		return database.query(r -> 1, hasDiscipline, id) != null;
	}

	public boolean hasStudent(UUID id) throws SQLException {
		return database.query(r -> 1, hasStudent, id) != null;
	}

	public boolean hasTeacher(UUID id) throws SQLException {
		return database.query(r -> 1, hasTeacher, id) != null;
	}

	private List<Mark> fill(List<Mark> simple) throws SQLException {
		for (Mark m : simple) {
			m.setStudent(studentRepository.get(m.getStudent().getId()));
		}

		return simple;
	}

	public List<Mark> listStudentMarks(UUID student) throws SQLException {
		return fill(database.queryList(Mark.class, getStudentMarks, student));
	}

	public List<Mark> listGroupMarks(UUID group) throws SQLException {
		return fill(database.queryList(Mark.class, getGroupMarks, group));
	}

	public boolean insertMark(Mark mark) throws SQLException {
		if (hasDiscipline(mark.getDiscipline().getId()) && hasStudent(mark.getStudent().getId())) {
			database.execute(insert, mark.getId(), mark.getStudent().getId(), mark.getDiscipline().getId(),
					mark.getMark());
			return true;
		}

		return false;
	}

	public boolean updateMark(Mark mark) throws SQLException {
		if (has(mark.getId()) && hasDiscipline(mark.getDiscipline().getId()) && hasStudent(mark.getStudent().getId())) {
			database.execute(update, mark.getMark(), mark.getId());
			return true;
		}

		return false;
	}

	public List<Discipline> listDisciplines() throws SQLException {
		return database.queryList(Discipline.class, listDisciplines);
	}

	public boolean insertDiscipline(Discipline discipline) throws SQLException {
		if (!hasTeacher(discipline.getTeacher().getId())) {
			return false;
		}
		
		database.execute(insertDiscipline, discipline.getId(), discipline.getName(), discipline.getTeacher().getId());
		return true;
	}
}
