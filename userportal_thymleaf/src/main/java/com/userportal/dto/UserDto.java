package com.userportal.dto;

import org.springframework.stereotype.Component;

@Component
public class UserDto {

	private Long id;
	private String email;
	private boolean unique;
	
	public UserDto() {
		// TODO Auto-generated constructor stub
	}
	
	
	public UserDto(Long id, String email, boolean unique) {
		super();
		this.id = id;
		this.email = email;
		this.unique = unique;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	public boolean isUnique() {
		return unique;
	}


	public void setUnique(boolean unique) {
		this.unique = unique;
	}

}
