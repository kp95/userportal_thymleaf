package com.userportal.login.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

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
		return oauth2User.getAuthorities();
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
