package com.students.service;

public class Result<T> {
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
}
