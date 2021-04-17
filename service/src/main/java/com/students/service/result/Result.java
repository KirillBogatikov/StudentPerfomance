package com.students.service.result;

public class Result<T> {
	private boolean notFound;
	private T data;
	private String error;
	
	public Result() {
		this(null);
	}
	
	public Result(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public boolean isNotFound() {
		return notFound;
	}

	public void setNotFound(boolean notFound) {
		this.notFound = notFound;
	}

	public boolean isSuccess() {
		return error == null && !notFound;
	}
}
