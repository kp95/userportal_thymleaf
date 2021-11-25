package com.userportal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.userportal.domain.AuthenticationType;
import com.userportal.domain.User;
import com.userportal.repository.UserRepository;
import com.userportal.service.UserServiceImpl;

@SpringBootApplication
public class UserportalThymleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserportalThymleafApplication.class, args);
	}

}
