package com.students.rest;

import java.io.IOException;
import java.sql.SQLException;

import javax.crypto.SecretKey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.students.db.sql.SqlDatabase;
import com.students.db.sql.SqlImporter;
import com.students.db.sql.TeacherRepository;
import com.students.db.sql.UserDataRepository;
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
public class Server extends WebMvcConfigurationSupport {
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
	
	public Server() {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(cfg.secret));
		database = new SqlDatabase(cfg.jdbc);
		
		var importer = new SqlImporter(cfg.salt, cfg.saltPosition, database);
		try {
			importer.importTeachers();
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Bean SqlDatabase database() {
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
	public ObjectMapper jacksonMapper() {
		return new ObjectMapper();
	}
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis((each) -> true)              
          .paths((each) -> each.startsWith("/api/"))                          
          .build();                                           
    }
	
	@Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

	public static void main(String[] args) throws Exception {
		Extractor.setLoader(Server.class.getClassLoader());
		
		var mapper = new ObjectMapper();
		cfg = mapper.readValue(args[0], Config.class);
		SpringApplication.run(Server.class, args);
	}

}
