package com.students.service.result;

import java.util.List;

public class ListResult<T> extends Result<List<T>> {
	private boolean queryIncorrect;
	
	public ListResult() {
		this(false);
	}
	
	public ListResult(boolean queryIncorrect) {
		super(null);
		this.queryIncorrect = queryIncorrect;
	}
	
	public ListResult(List<T> data) {
		super(data);
	}
	
	public ListResult(String error) {
		super(null);
		setError(error);
	}
	
	public boolean isQueryIncorrect() {
		return queryIncorrect;
	}

	public void setQueryIncorrect(boolean queryIncorrect) {
		this.queryIncorrect = queryIncorrect;
	}
	
	@Override
	public boolean isSuccess() {
		return super.isSuccess() && !queryIncorrect;
	}
		
}
