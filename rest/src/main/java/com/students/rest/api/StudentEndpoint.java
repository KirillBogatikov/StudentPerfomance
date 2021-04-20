package com.students.rest.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.students.db.model.Student;
import com.students.db.model.Teacher;
import com.students.service.StudentService;

@RestController
@RequestMapping("api/student")
public class StudentEndpoint extends AuthorizedEndpoint {
	@Autowired
	private StudentService service;
	
	@GetMapping(path = {"search", "list"})
	public ResponseEntity<?> list(@RequestHeader("Authorization") String token, String query, int offset, int limit) {
		ResponseEntity<List<Teacher>> status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.list(query, offset, limit);
		if (result.isNotFound()) {
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
	
	@GetMapping("{id}/group")
	public ResponseEntity<?> getGroup(@RequestHeader("Authorization") String token, @PathVariable String id) {
		ResponseEntity<List<Teacher>> status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.getStudentGroup(id);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) {
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping
	public ResponseEntity<?> save(@RequestHeader("Authorization") String token, @RequestBody Student s) {
		ResponseEntity<List<Teacher>> status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.save(s);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		if (!result.isValid()) {
			return new ResponseEntity<>(result.getValidation(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable String id) {
		ResponseEntity<List<Teacher>> status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.delete(id);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (result.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
