package com.students.service.result;

import java.util.Map;
import java.util.UUID;

import com.students.service.validation.ValidationResult;

public class SaveResult extends Result<UUID> {
	private boolean objectFound;
	private Map<String, ValidationResult> validation;
	
	public SaveResult(UUID data) {
		this(data, true);
	}
	
	public SaveResult(UUID data, boolean objectFound) {
		super(data);
		setObjectFound(objectFound);
	}
	
	public SaveResult(String error) {
		super(null);
		setError(error);
	}
	
	public SaveResult(Map<String, ValidationResult> validation) {
		super(null);
		setValidation(validation);
	}

	public boolean isObjectFound() {
		return objectFound;
	}
	
	public void setObjectFound(boolean objectFound) {
		this.objectFound = objectFound;
	}
	
	public Map<String, ValidationResult> getValidation() {
		return validation;
	}

	public void setValidation(Map<String, ValidationResult> validation) {
		this.validation = validation;
	}

	public boolean isValid() {
		return validation == null || validation.isEmpty();
	}
	
	public boolean isSuccess() {
		return super.isSuccess() && isValid() && getData() != null && objectFound;
	}
}
