package com.students.rest.api;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.students.db.model.Discipline;
import com.students.db.model.Mark;
import com.students.service.MarkService;
import com.students.service.PerfomanceService;
import com.students.service.result.ListResult;
import com.students.service.result.SaveResult;

@RestController
@RequestMapping("api/mark")
public class MarkEndpoint extends AuthorizedEndpoint {
	@Autowired
	private MarkService service;
	@Autowired
	private PerfomanceService perfomance;
	
	@GetMapping("discipline")
	public ResponseEntity<?> listDisciplines(@RequestHeader("Authorization") String token) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		ListResult<Discipline> result = service.listDisciplines();
		
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) { 
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}	
	
	@PostMapping("discipline")
	public ResponseEntity<?> addDiscipline(@RequestHeader("Authorization") String token, @RequestBody Discipline discipline) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		SaveResult result = service.addDiscipline(discipline);
		
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) { 
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}	
	
	@GetMapping("student/{studentId}")
	public ResponseEntity<?> listStudentMarks(@RequestHeader("Authorization") String token, @PathVariable String studentId, @RequestParam(required=false) List<String> disciplines) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		ListResult<Mark> result = service.listStudentMarks(studentId, disciplines);
		
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) { 
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
	@GetMapping("group/{groupId}")
	public ResponseEntity<?> listGroupMarks(@RequestHeader("Authorization") String token, @PathVariable String groupId, @RequestParam(required=false) List<String> disciplines) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		ListResult<Mark> result = service.listGroupMarks(groupId, disciplines);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) { 
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping()
	public ResponseEntity<?> saveMark(@RequestHeader("Authorization") String token, @RequestBody Mark mark) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		SaveResult result = service.saveMark(mark);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (!result.isValid()) {
			return new ResponseEntity<>(result.getValidation(), HttpStatus.BAD_REQUEST);
		}
		
		if (result.isSuccess()) { 
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("export/group/{id}")
	public ResponseEntity<?> exportGroup(@RequestHeader("Authorization") String token, @PathVariable String id) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		SaveResult result = perfomance.exportGroup(id);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) { 
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("export/student/{id}")
	public ResponseEntity<?> exportStudent(@RequestHeader("Authorization") String token, @PathVariable String id) {
		var status = auth(token);
		if (status != null) {
			return status;
		}
		
		SaveResult result = perfomance.exportStudent(id);
		if (result.isNotFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (result.isSuccess()) { 
			return new ResponseEntity<>(result.getData(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("files/{id}")
	public void getFile(@PathVariable String id, HttpServletResponse response) {
		var file = new File("reports/xlsx/group", "%s.xlsx".formatted(id));
		System.out.println(file);
		
		if (!file.exists()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		try (InputStream is = new FileInputStream(file);
			 BufferedInputStream input = new BufferedInputStream(is)) {
			var bytes = new ByteArrayOutputStream();
			var buffer = new byte[8192];
			int count = 0;
			
			while((count = input.read(buffer)) > -1) {
				bytes.write(buffer, 0, count);
			}
			
			response.setHeader("Content-Disposition", "attachment; filename=\"report.xlsx\"");
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setStatus(200);
			response.getOutputStream().write(bytes.toByteArray());
		} catch(Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}
}
