package com.userportal.login.oauth2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.userportal.domain.AuthenticationType;
import com.userportal.domain.User;
import com.userportal.service.UserServiceImpl;
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	@Autowired private UserServiceImpl service;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		
		LoginOAuth2User auth2User = (LoginOAuth2User) authentication.getPrincipal();
		//System.out.println(authentication.getPrincipal());
		String name = auth2User.getName();
		String email = auth2User.getEmail();
		
		User user = service.findByEmail(email);
		
		if(user == null) {
			service.addOAuthUser(name, email);
		}
		else {
			service.updateAuthenticatioType(user, AuthenticationType.GOOGLE);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
		
		
	}
}
