package com.userportal.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.userportal.domain.AuthenticationType;
import com.userportal.domain.User;
import com.userportal.domain.UserPrincipal;
import com.userportal.login.oauth2.LoginOAuth2User;
import com.userportal.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl {
	
	private UserRepository repo;
	private BCryptPasswordEncoder passwordEncoder;
	public static final int USERS_PER_PAGE = 4;
	
	@Autowired
	public UserServiceImpl(UserRepository repo, BCryptPasswordEncoder passwordEncoder) {
		super();
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User register(User user) {
		
		User loggedUser = null;
		User parentUser = null;
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
			UserPrincipal temp = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			parentUser = repo.findById(temp.getId()).get();
			loggedUser = parentUser;
		}
		else {
			List<User> allAdmins = repo.findAllManager();
			parentUser = allAdmins.get(0);
			//logged user
			LoginOAuth2User temp = (LoginOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			loggedUser = repo.findUserByEmail(temp.getEmail());
			
		}
		if(user.getId() == null) {
			user.setAuthenticationType(AuthenticationType.DATABASE);
			user.setUsername((user.getFirstName()+"_"+user.getLastName()).toString());
			String password = passwordEncoder.encode(user.getPassword());
			user.setPassword(password);
			if(user.getManageBy() == null) {
				user.setManageBy(parentUser);
			}
			return repo.save(user);
		}
		else {
			User existingUser = repo.findById(user.getId()).get();
			
			if(loggedUser.getRole().equals("user")) {
				user.setActive(existingUser.isActive());
				user.setRole(existingUser.getRole());
			}
			user.setAuthenticationType(existingUser.getAuthenticationType());
			if(!user.getPassword().isEmpty()) {
				String password = passwordEncoder.encode(user.getPassword());
				user.setPassword(password);
			}
			else {
				user.setPassword(existingUser.getPassword());
			}
			if(user.getManageBy() == null) {
				user.setManageBy(existingUser.getManageBy());
			}
			user.setUsername(existingUser.getUsername());
			user.setJoinDate(existingUser.getJoinDate());
			user.setLastLoginDate(existingUser.getLastLoginDate());
			
			return repo.save(user);
		}
	}
	
	public User findByEmail(String email) {
		//repo.
		return repo.findUserByEmail(email);
	}
	
	
	public User findByUsername(String username) {
		return repo.findUserByUsername(username);
	}
	public Page<User> findAllUser(int pageNum, String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1,USERS_PER_PAGE ,sort);
		
		User loggedUser = null;
		
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
			UserPrincipal temp = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//SecurityContextHolder.getContext().setAuthentication(null);
			loggedUser = repo.findById(temp.getId()).get();
		}
		else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof LoginOAuth2User) {
			//return repo.findAllByRole("user", pageable);
			LoginOAuth2User temp = (LoginOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			loggedUser = repo.findUserByEmail(temp.getEmail());
		}
		
		
		if(loggedUser.getRole().equals("user")) {
			if(keyword != null) {
				//for searching we use keyword
				return repo.findAllByRole(keyword,"user",pageable);
			}
			return repo.findAllByRole("user", pageable);
		}
		else if(loggedUser.getRole().equals("manager")) {
			User parentUser = repo.findById(loggedUser.getId()).get();
			if(keyword != null) {
				//for searching we use keyword
				return repo.findAllByManager(keyword, parentUser, pageable);
			}
			return repo.findAllByManager(parentUser, pageable);
		}
		else {
			if(keyword != null) {
				//for searching we use keyword
				return repo.findAll(keyword,pageable);
			}
			return repo.findAll(pageable);
		}
		
	}
	public List<User> listAll(){
		return (List<User>) repo.findAll();
	}
	public List<User> listAll(User manager){
		return (List<User>) repo.findAll(manager);
	}
	
	public void delete(Long id) {
		repo.deleteById(id);
	}
	
	public boolean checkEmail(Long id,String email) {
		User userByEmail = repo.findUserByEmail(email);
		
		if (userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if (isCreatingNew) {
			if (userByEmail != null) return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}
	
	public void updateStaus(Long id, boolean status) {
		repo.updateStatus(id, status);
	}
	public void deleteUser(Long id) throws Exception {
		Optional<User> user = repo.findById(id);
		if(user.get() != null) {
			repo.delete(user.get());
		}
		else {
			throw new Exception("User not Exist");
		}
		
	
	}
	
	public User findById(Long id) {
		Optional<User> user = repo.findById(id);
		if(user.get() != null) {
			return user.get();
		}
		else {
			return null;
		}
	}
	public List<User> findAllManager(){
		return repo.findAllManager();
	}
	public List<User> findAllAdmin(){
		return repo.findAllAdmin();
	}
	
	public User addOAuthUser(String name,String email) {
		
		List<User> allAdmins = repo.findAllAdmin();
		
		User user = new User();
		user.setEmail(email);
		user.setFirstName(name);
		user.setLastName("");
		user.setActive(true);
		user.setRole("user");
		user.setUsername(name);
		user.setAuthenticationType(AuthenticationType.GOOGLE);
		user.setManageBy(allAdmins.get(0));
		
		return repo.save(user);
	}
	public void updateAuthenticatioType(User user, AuthenticationType type) {
		if(!user.getAuthenticationType().equals(type)) {
			repo.updateAuthenticationType(user.getId(), type);
		}
	}
	public List<User> findByAdminManager() {
		return repo.findByAdminManager();
	}
	
}
