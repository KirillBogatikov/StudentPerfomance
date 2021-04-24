package com.students.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import javax.crypto.SecretKey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.students.db.migrations.Migrator;
import com.students.db.sql.GroupRepository;
import com.students.db.sql.MarkRepository;
import com.students.db.sql.SqlDatabase;
import com.students.db.sql.StudentRepository;
import com.students.db.sql.TeacherRepository;
import com.students.db.sql.UserDataRepository;
import com.students.service.GroupService;
import com.students.service.MarkService;
import com.students.service.PerfomanceService;
import com.students.service.StudentService;
import com.students.service.TeacherService;
import com.students.service.auth.AuthService;
import com.students.util.Extractor;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
public class Server {
	public static class Config {
		public String secret;
		public String salt;
		@JsonProperty("salt_position")
		public int saltPosition;
		public String jdbc;
		public String host;
	}

	private static Config cfg;
	private SecretKey key;
	private SqlDatabase database;
	private AuthService authService;
	private TeacherService teacherService;
	private GroupService groupService;
	private StudentService studentService;
	private MarkService markService;
	private PerfomanceService perfomanceService;

	public Server() {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(cfg.secret));
		database = new SqlDatabase(cfg.jdbc);

		try {
			var migrator = new Migrator(cfg.salt, cfg.saltPosition, database);
			migrator.migrate();
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public SqlDatabase database() {
		return database;
	}

	@Bean
	public AuthService auth() {
		if (authService == null) {
			var repo = new UserDataRepository(database);
			authService = new AuthService(key, cfg.salt, cfg.saltPosition, repo);
		}

		return authService;
	}

	@Bean
	public TeacherService teachers() {
		if (teacherService == null) {
			var repo = new TeacherRepository(database);
			teacherService = new TeacherService(repo, cfg.salt, cfg.saltPosition);
		}

		return teacherService;
	}

	@Bean
	public GroupService groups() {
		if (groupService == null) {
			var repo = new GroupRepository(database);
			groupService = new GroupService(repo);
		}

		return groupService;
	}

	@Bean
	public StudentService students() {
		if (studentService == null) {
			var repo = new StudentRepository(database);
			studentService = new StudentService(repo);
		}

		return studentService;
	}

	@Bean
	public MarkService marks() {
		if (markService == null) {
			var repo = new MarkRepository(database);
			markService = new MarkService(repo);
		}

		return markService;
	}

	@Bean
	public PerfomanceService perfomance() {
		if (perfomanceService == null) {
			var marks = new MarkRepository(database);
			var groups = new GroupRepository(database);
			var students = new StudentRepository(database);
			perfomanceService = new PerfomanceService(marks, groups, students);
		}

		return perfomanceService;
	}

	@Bean
	public ObjectMapper jacksonMapper() {
		return new ObjectMapper();
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis((each) -> true)
				.paths((each) -> each.startsWith("/api/")).build();
	}

	public static void main(String[] args) throws Exception {
		Extractor.setLoader(Server.class.getClassLoader());

		if (args.length > 0) {
			var mapper = new ObjectMapper();
			cfg = mapper.readValue(args[0], Config.class);
		} else {
			cfg = new Config();
			cfg.jdbc = System.getenv("DATABASE_URL");
			cfg.host = System.getenv("SERVER_HOST");
			cfg.secret = System.getenv("SECURITY_SECRET");
			cfg.salt = System.getenv("SECURITY_SALT");
			cfg.saltPosition = Integer.valueOf(System.getenv("SECURITY_SALT_INDEX"));
		}
		SpringApplication.run(Server.class, args);
		
		var thread = new Thread(() -> {
			var scanner = new Scanner(System.in);
			String cmd;
			
			while ((cmd = scanner.next()) != null) {
				switch(cmd) {
					case "exit": System.exit(0); 
				}
			}
			
			scanner.close();
		});
		thread.start();
	}

}
