package com.students.db.sql;

import static com.students.util.Extractor.readText;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Discipline;
import com.students.db.model.Group;
import com.students.db.model.Plan;
import com.students.db.model.Teacher;
import com.students.db.repo.Database;

public class PlanRepository extends SqlRepository {
	private static final String DIR = "sql/plan";
	private static final String forGroup = readText(DIR, "for_group.sql"), has = readText(DIR, "has.sql"),
			hasDiscipline = readText(DIR, "discipline/has.sql"), list = readText(DIR, "discipline/list.sql"),
			insert = readText(DIR, "insert.sql"), insertDiscipline = readText(DIR, "discipline/insert.sql"),
			updateDiscipline = readText(DIR, "update.sql"), delete = readText(DIR, "delete.sql"),
			deleteDiscipline = readText(DIR, "discipline/delete.sql");

	public PlanRepository(Database database) {
		super(database);

		mapping.register(Discipline.class, r -> {
			var d = new Discipline();

			d.setId(r.getObject("discipline_id", UUID.class));
			d.setName(r.getString("discipline_name"));
			d.setHours(r.getInt("discipline_hours"));
			d.setTeacher(mapping.forType(Teacher.class).process(r));

			return d;
		});
		mapping.register(Plan.class, r -> {
			var p = new Plan();

			p.setId(r.getObject("plan_id", UUID.class));
			p.setSemester(r.getInt("plan_semester"));
			p.setGroup(mapping.forType(Group.class).process(r));

			return p;
		});
	}

	public List<Discipline> listDisciplines(UUID id) throws SQLException {
		return database.queryList(Discipline.class, list, id);
	}

	public Plan forGroup(UUID group) throws SQLException {
		var plan = database.query(Plan.class, forGroup, group);
		if (plan != null) {
			plan.setDisciplines(listDisciplines(plan.getId()));
		}

		return plan;
	}

	public boolean hasDiscipline(UUID id) throws SQLException {
		return database.query(r -> 1, hasDiscipline, id) != null;
	}

	public boolean has(UUID id) throws SQLException {
		return database.query(r -> 1, has, id) != null;
	}

	public void insert(Plan plan) throws SQLException {
		database.execute(insert, plan.getId(), plan.getGroup().getId(), plan.getSemester());
	}

	public boolean insertDiscipline(UUID plan, Discipline d) throws SQLException {
		if (!has(plan)) {
			return false;
		}

		database.execute(insertDiscipline, d.getId(), plan, d.getTeacher().getId(), d.getName(), d.getHours());
		return true;
	}

	public boolean updateDiscipline(Discipline d) throws SQLException {
		if (!hasDiscipline(d.getId())) {
			return false;
		}

		database.execute(updateDiscipline, d.getTeacher().getId(), d.getName(), d.getHours(), d.getId());
		return true;
	}

	public boolean deleteDiscipline(UUID plan, UUID discipline) throws SQLException {
		if (!has(plan)) {
			return false;
		}

		database.execute(deleteDiscipline, discipline);
		return true;
	}

	public boolean delete(UUID id) throws SQLException {
		if (!has(id)) {
			return false;
		}

		database.execute(delete, id);
		return true;
	}
}
