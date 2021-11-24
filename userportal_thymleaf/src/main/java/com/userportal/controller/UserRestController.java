package com.userportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userportal.dto.UserDto;
import com.userportal.service.UserServiceImpl;

@RestController
public class UserRestController {
	
	@Autowired private UserServiceImpl service;
	
	
	@PostMapping("/checkUserEamil")
	public ResponseEntity<UserDto> checkEmail(@Param("id") Long id,@Param("email") String email) {
		//System.out.println(email);
		boolean unique = service.checkEmail(id, email);
		UserDto response = new UserDto(id, email, unique);
		return new ResponseEntity<UserDto>(response,HttpStatus.OK);
	}
}
