package com.students.rest.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.students.db.model.Teacher;
import com.students.service.ListResult;
import com.students.service.TeacherService;

@RestController
@RequestMapping("api/teacher")
public class TeacherEndpoint extends AuthorizedEndpoint {
	@Autowired
	private TeacherService service;

	@GetMapping(path = {"/list", "/search"})
	public ResponseEntity<List<Teacher>> list(@RequestHeader("Authorization") String token, String query, int offset, int limit) {
		ResponseEntity<List<Teacher>> status = auth(token);
		if (status != null) {
			return status;
		}
		
		ListResult<Teacher> result;
		if (query == null) {
			result = service.list(offset, limit);
		} else {
			result = service.search(query, offset, limit);
		}

		if (result.getError() != null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (result.isBoundsIncorrect() || result.isQueryIncorrect()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(result.getData());
	}
	
	private ResponseEntity<Map<String, Object>> save(Teacher teacher) {
		var result = service.save(teacher);
		var map = result.getData();
		if (map.isEmpty()) {
			map.put("id", teacher.getId());
			return new ResponseEntity<>(map, HttpStatus.OK);
		}
		return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("")
	public ResponseEntity<Map<String, Object>> create(@RequestHeader("Authorization") String token, @RequestBody Teacher teacher) {
		ResponseEntity<Map<String, Object>> status = auth(token);
		if (status != null) {
			return status;
		}
		
		return save(teacher);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Map<String, Object>> update(@RequestHeader("Authorization") String token, @RequestBody Teacher teacher, String id) {
		ResponseEntity<Map<String, Object>> status = auth(token);
		if (status != null) {
			return status;
		}
		
		return save(teacher);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@RequestHeader("Authorization") String token, String id) {
		ResponseEntity<Void> status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.delete(UUID.fromString(id));
		if (result.getError() != null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (result.getData()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
