package com.students.service.validation;

import static com.students.service.validation.ValidationResult.Incorrect;
import static com.students.service.validation.ValidationResult.TooLong;
import static com.students.service.validation.ValidationResult.TooShort;
import static com.students.service.validation.ValidationResult.Valid;

public class Validator {
	public static ValidationResult validate(String value, int min, int max, String regex) {
		if (value.length() < min) {
			return TooShort;
		}
		if (value.length() > max) {
			return TooLong;
		}
		
		if (value.matches(regex)) {
			return Valid;
		}
		
		return Incorrect;
	}
	
	public static boolean isBoundsCorrect(int offset, int limit) {
		return offset >= 0 && limit >= 1 && limit <= 1000;
	}
	
	public static boolean isQuerySafe(String query) {
		return !query.toLowerCase().matches(".*(select|drop|create|table|insert|delete|update|truncate).*");
	}
}
