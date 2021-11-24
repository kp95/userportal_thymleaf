package com.userportal.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.userportal.domain.User;
import com.userportal.domain.UserPrincipal;
import com.userportal.repository.UserRepository;

@Component
public class UserLoginService implements UserDetailsService{

	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = repo.findUserByEmail(email);
		if(user != null) {
			return new UserPrincipal(user);
		}
		throw new UsernameNotFoundException("Could not Find user with  "+ email);
	}

}
