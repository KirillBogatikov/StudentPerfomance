package com.students.service.auth;

import java.sql.SQLException;
import java.util.Arrays;

import javax.crypto.SecretKey;

import com.students.db.model.Auth;
import com.students.db.sql.UserDataRepository;
import com.students.util.Hash;

public class AuthService {
	private UserDataRepository repository;
	private SecretKey secretKey;
	private String salt;
	private int saltPosition;
	
	public AuthService(SecretKey secretKey, String salt, int saltPosition, UserDataRepository repository) {
		this.secretKey = secretKey;
		this.repository = repository;
		this.salt = salt;
		this.saltPosition = saltPosition;
	}
	
	public LoginResult login(String login, String password) {
		var result = new LoginResult();
		Auth auth;
		try {
			auth = repository.get(login);
		} catch (SQLException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
			return result;
		}
		
		if (auth == null) {
			return result;
		}
		
		result.setUserFound(true);
		
		if (Arrays.equals(auth.getPasswordHash(), Hash.hash(password, salt, saltPosition))) {
			var token = new Token(secretKey, auth.getId());
			result.setData(token);
		} else {
			result.setPasswordIncorrect(true);
		}
		
		return result;
	}
	
	public LoginResult login(String tokenString) {
		var result = new LoginResult();
		Token token;
		try {
			token = Token.decode(secretKey, tokenString);
		} catch(Exception e) {
			e.printStackTrace();
			result.setInvalidToken(true);
			return result;
		}
		
		try {
			var found = repository.has(token.getUserId());
			result.setUserFound(found);
				
			if (!found) {
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
			return result;
		}
		
		result.setData(token);
		return result;
	}
	
}
