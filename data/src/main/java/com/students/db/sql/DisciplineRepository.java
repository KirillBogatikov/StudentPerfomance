package com.students.db.sql;

import static com.students.util.Extractor.readText;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.students.db.model.Discipline;
import com.students.db.model.Plan;
import com.students.db.repo.Database;

public class DisciplineRepository extends SqlRepository {
	private static final String DIR = "sql/discipline";
	private static final String has = readText(DIR, "has.sql"),
								list = readText(DIR, "list.sql"),
								search = readText(DIR, "search.sql"),
								insert = readText(DIR, "insert.sql"),
								update = readText(DIR, "update.sql"),
								delete = readText(DIR, "delete.sql");

	public DisciplineRepository(Database database) {
		super(database);
		
		mapping.register(Discipline.class, r -> {
			var d = new Discipline();
			
			d.setId(r.getObject("discipline_id", UUID.class));
			d.setName(r.getString("discipline_name"));
			
			return d;
		});
		mapping.register(Plan.class, r -> {
			var p = new Plan();
			
			return p;
		});
	}

	public List<Discipline> list(int offset, int limit) throws SQLException {
		return database.queryList(Discipline.class, list, offset, limit);
	}

	public List<Discipline> search(String query, int offset, int limit) throws SQLException {
		return database.queryList(Discipline.class, search, query, offset, limit);
	}

	public void insert(Discipline d) throws SQLException {
		database.execute(insert, d.getId(), d.getName());
	}
	
	public boolean update(Discipline d) throws SQLException {
		if (database.query(r -> 1, has, d.getId()) == null) {
			return false;
		}
		
		database.execute(update, d.getName(), d.getId());
		return true;
	}

	public void delete(UUID id) throws SQLException {
		database.execute(delete, id);
	}
}
