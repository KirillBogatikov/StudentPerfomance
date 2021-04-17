package com.students.rest.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.students.db.model.Teacher;
import com.students.service.TeacherService;

@RestController
@RequestMapping("api/teacher")
public class TeacherEndpoint extends AuthorizedEndpoint {
	@Autowired
	private TeacherService service;

	@GetMapping(path = {"list", "search"})
	public ResponseEntity<List<Teacher>> list(@RequestHeader("Authorization") String token, String query, int offset, int limit) {
		ResponseEntity<List<Teacher>> status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.list(query, offset, limit);
		if (result.getData() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) {
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		if (result.isQueryIncorrect()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping()
	public ResponseEntity<?> save(@RequestHeader("Authorization") String token, @RequestBody Teacher teacher) {
		ResponseEntity<Map<String, Object>> status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.save(teacher);
		if (result.isSuccess()) {
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (!result.isValid()) {
			return new ResponseEntity<>(result.getValidation(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@RequestHeader("Authorization") String token, String id) {
		ResponseEntity<Void> status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.delete(UUID.fromString(id));
		if (result == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
