package com.students.rest.api;

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

import com.students.db.model.Group;
import com.students.service.GroupService;
import com.students.service.result.ListResult;
import com.students.service.result.SaveResult;

@RestController
@RequestMapping("api/group")
public class GroupEndpoint extends AuthorizedEndpoint {
	@Autowired
	private GroupService service;
	
	@GetMapping(path = {"list", "search"})
	public ResponseEntity<?> list(@RequestHeader("Authorization") String token, String query, int offset, int limit) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		ListResult<Group> result = service.list(query, offset, limit);
		if (result.isSuccess()) {
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		if (result.isQueryIncorrect()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("{id}/students")
	public ResponseEntity<?> getStudents(@RequestHeader("Authorization") String token, String id) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.listStudents(id);
		if (result.getData() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) {
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping()
	public ResponseEntity<?> save(@RequestBody Group g) {
		SaveResult result = service.save(g);
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
	public ResponseEntity<?> delete(String id) {
		var error = service.delete(id);
		if (error == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
