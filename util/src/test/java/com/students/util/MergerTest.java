package com.students.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MergerTest {
	public static class Entity {
		private String name;
		private int age;
		
		public Entity(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public int getAge() {
			return age;
		}
		
		public void setAge(int age) {
			this.age = age;
		}
		
	}
	
	@Test
	void testMerge() {
		var entity = new Entity(null, 26);
		Merger.merge(s -> entity.setName(s), () -> entity.getName(), () -> "Olga");
		Merger.merge(i -> entity.setAge(i), () -> entity.getAge(), () -> entity.getAge());
		
		assertEquals("Olga", entity.getName());
		assertEquals(26, entity.getAge());
	}

}
