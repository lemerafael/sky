package com.sky.projects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.sky.projects.security.PasswordEncoderConfig;

@SpringBootApplication
@Import(PasswordEncoderConfig.class)
public class ProjectsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectsApplication.class, args);
	}

}
