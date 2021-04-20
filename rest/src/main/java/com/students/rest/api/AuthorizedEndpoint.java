package com.students.rest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.students.service.auth.AuthService;

public abstract class AuthorizedEndpoint {
	@Autowired
	private AuthService authService;
	
	public <T> ResponseEntity<T> auth(String header) {
		if (header == null || !header.startsWith("Bearer ")) {
			//No header or header invalid
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		var result = authService.login(header.split("Bearer ")[1]);
		if (result.isInvalidToken()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		if (!result.isUserFound()) {
			//Old token for deleted user
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.getError() != null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (result.getData() == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		if (result.getData().isValid()) {
			return null;
		}
		
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
}
