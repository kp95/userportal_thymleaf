package com.userportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.userportal.domain.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>{

	User findUserByEmail(String email);
	User findUserByUsername(String username);
	

	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ',"
			+ " u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Modifying
	@Query("UPDATE User u SET u.active = ?2 WHERE u.id = ?1")
	public void updateStatus(Long id, boolean status);
	
	@Query("SELECT u FROM User u WHERE u.role = 'manager'")
	public List<User> findAllManager();
	
	@Query("SELECT u FROM User u WHERE u.role = 'admin'")
	public List<User> findAllAdmin();
}
