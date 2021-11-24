package com.userportal.main;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import com.userportal.domain.UserPrincipal;

public class LoginSuccessHandler implements AuthenticationSuccessHandler{

	Logger LOGGER = org.slf4j.LoggerFactory.getLogger(getClass());
	private RedirectStrategy redirectStartegy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		handle(request, response, authentication);
	}
	
	private void handle(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		
		LOGGER.info("Fecthing redirect URL's");
		
		String targetURL = targetUrl(authentication);
		if(response.isCommitted()) {
			LOGGER.debug(
	                "Response has already been committed. Unable to redirect to "
	                        + targetURL);
	        return;
		}
		
		redirectStartegy.sendRedirect(request, response, targetURL);
		
	}
	private String targetUrl(final Authentication authentication) {
		
		Long id = null;
		if(authentication.getPrincipal() instanceof UserPrincipal) {
			UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
			id = user.getId();
		}
		
		
		Map<String, String> roleToUrl = new HashMap<>();
		roleToUrl.put("user", "/logUser/edit/"+ id);
		roleToUrl.put("admin", "/users/");
		 
		
		final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (final GrantedAuthority grantedAuthority : authorities) {
	        String authorityName = grantedAuthority.getAuthority();
	        if(roleToUrl.containsKey(authorityName)) {
	            return roleToUrl.get(authorityName);
	        }
	    }
		return "/users";
	    //throw new IllegalStateException();
 	}
	protected void clearAuthenticationAttributes(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session == null) {
	        return;
	    }
	    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
