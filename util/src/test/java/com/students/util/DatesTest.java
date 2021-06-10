package com.students.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class DatesTest {
	@Test
	void testPlusHours() {
		var now = Dates.now();
		assertEquals((now.getHours() + 12) % 24, Dates.plusHours(now, 12).getHours());
	}
}
