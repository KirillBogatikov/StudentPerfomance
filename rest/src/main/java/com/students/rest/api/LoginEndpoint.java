package com.students.rest.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.students.rest.model.Credentials;
import com.students.service.auth.AuthService;

@RestController
@RequestMapping("api/auth")
public class LoginEndpoint {
	@Autowired
	private AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody Credentials credentials) {
		var result = authService.login(credentials.getLogin(), credentials.getPassword());
		
		if (!result.isUserFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isPasswordIncorrect()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		var map = new HashMap<String, String>();
		if (result.getError() != null) {
			map.put("error", result.getError());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		map.put("token", result.getData().encode());
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
