package com.students.service.auth;

import com.students.service.result.Result;

public class LoginResult extends Result<Token> {
	private boolean userFound;
	private boolean passwordIncorrect;
	
	public boolean isUserFound() {
		return userFound;
	}
	
	public void setUserFound(boolean userFound) {
		this.userFound = userFound;
	}

	public boolean isPasswordIncorrect() {
		return passwordIncorrect;
	}

	public void setPasswordIncorrect(boolean passwordIncorrect) {
		this.passwordIncorrect = passwordIncorrect;
	}
	
}
