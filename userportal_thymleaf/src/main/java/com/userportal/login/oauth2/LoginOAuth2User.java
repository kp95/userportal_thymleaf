package com.userportal.login.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.userportal.domain.User;
import com.userportal.service.UserServiceImpl;

public class LoginOAuth2User implements OAuth2User{

	private OAuth2User oauth2User;
	 
	
	
	public LoginOAuth2User(OAuth2User oauth2User) {
		super();
		this.oauth2User = oauth2User;
	}

	

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		//User storedOAuth2User = service.findByEmail(getEmail());
		/*
		 * User storedOAuth2User = service.findByEmail(getEmail()); if(storedOAuth2User
		 * != null) { authorities.add(new
		 * SimpleGrantedAuthority(storedOAuth2User.getRole())); } else {
		 * authorities.add(new SimpleGrantedAuthority("user")); }
		 */
		authorities.add(new SimpleGrantedAuthority("user"));
		return authorities;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return oauth2User.getAttribute("name");
	}
	public String getEmail() {
		return oauth2User.getAttribute("email");
	}
	
	public String getUsername() {
		// TODO Auto-generated method stub
		return oauth2User.getAttribute("name");
	}

}
