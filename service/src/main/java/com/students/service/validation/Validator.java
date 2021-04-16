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
}
