package com.students.db.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.students.db.repo.Mapper;

public class Mapping {
	private static Mapping instance;
	
	public static Mapping getInstance() {
		if (instance == null) {
			instance = new Mapping();
		}
		
		return instance;
	}
	
	private Map<Class<?>, Mapper<?>> mappers;
	
	private Mapping() {
		mappers = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public <T> Mapper<T> forType(Class<T> type) {
		return (Mapper<T>) mappers.get(type);
	}
	
	public <T> Mapper<List<T>> forList(Class<T> type) {
		var mapper = forType(type);
		return r -> {
			var list = new ArrayList<T>();
			
			do {
				list.add(mapper.process(r));
			} while(r.next());
			
			return list;
		};
	}
	
	public <T> void register(Class<T> type, Mapper<T> mapper) {
		mappers.put(type, mapper);
	}
	
	public void unregister(Class<?> type) {
		mappers.remove(type);
	}
}
