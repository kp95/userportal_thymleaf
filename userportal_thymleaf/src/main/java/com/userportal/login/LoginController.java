package com.userportal.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.userportal.domain.User;
import com.userportal.domain.UserPrincipal;
import com.userportal.service.UserServiceImpl;

@Controller
public class LoginController {

	//@Autowired private UserServiceImpl service;
	@GetMapping("/login")
	public String loginPage() {
		
		//User user = new User("Romi", "Anttal", "romi_anttal", "123456", "penduanttal@gmail.com", null, null, "admin", true, null);
		//service.register(user);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/";
	}
}
