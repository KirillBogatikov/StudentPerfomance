package com.students.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class HashTest {

	@Test
	void testHashPassword() {
		var hash = Hash.hash("12345678", "salt", 3);
		assertArrayEquals(new byte[] { 
			-49, -15, 2, 17, 94, -84, -2, 105, 
			7, 61, -126, -26, 86, 21, -64, -34, 
			-94, -61, 99, -45, -124, -80, -34, 33, 
			-36, 74, -12, 93, -128, 51, 67, -21 }, hash);
	}

}
