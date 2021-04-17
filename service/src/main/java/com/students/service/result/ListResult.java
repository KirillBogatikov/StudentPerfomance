package com.students.service;

import java.util.List;

public class ListResult<T> extends Result<List<T>> {
	private boolean queryIncorrect;
	private boolean boundsIncorrect;
	
	public boolean isQueryIncorrect() {
		return queryIncorrect;
	}

	public void setQueryIncorrect(boolean queryIncorrect) {
		this.queryIncorrect = queryIncorrect;
	}

	public boolean isBoundsIncorrect() {
		return boundsIncorrect;
	}

	public void setBoundsIncorrect(boolean boundsIncorrect) {
		this.boundsIncorrect = boundsIncorrect;
	}
		
}
