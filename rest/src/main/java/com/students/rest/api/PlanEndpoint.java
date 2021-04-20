package com.students.rest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.students.db.model.Discipline;
import com.students.db.model.Plan;
import com.students.service.PlanService;

@RestController
@RequestMapping("/api/plan")
public class PlanEndpoint extends AuthorizedEndpoint {
	@Autowired
	private PlanService service;
		
	@PutMapping
	public ResponseEntity<?> save(@RequestHeader String token, @RequestBody Plan plan) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.save(plan);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (result.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping("{planId}/discipline")
	public ResponseEntity<?> saveDiscipline(@RequestHeader String token, @RequestBody Discipline discipline, @PathVariable String planId) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.saveDiscipline(planId, discipline);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping("{planId}/discipline/{disciplineId}")
	public ResponseEntity<?> deleteDiscipline(@RequestHeader String token, @PathVariable String planId, @PathVariable String disciplineId) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.deleteDiscipline(planId, disciplineId);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping("{planId}")
	public ResponseEntity<?> delete(@RequestHeader String token, @PathVariable String planId) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		var result = service.delete(planId);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
