package com.userportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.userportal.domain.AuthenticationType;
import com.userportal.domain.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>{

	User findUserByEmail(String email);
	User findUserByUsername(String username);
	

	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ',"
			+ " u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ',"
			+ " u.lastName) LIKE %?1% AND u.role = ?2")
	public Page<User> findAllByRole(String keyword,String role, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.role = ?1")
	public Page<User> findAllByRole(String role, Pageable pageable);
	
	 @Query(value ="SELECT u FROM User u WHERE u.manageBy = ?1",nativeQuery = false) 
	 public Page<User> findAllByManager(User parentUser, Pageable
	  pageable);
 
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ',"
				+ " u.lastName) LIKE %?1% AND u.manageBy = ?2")
	public Page<User> findAllByManager(String keyword,User parentUser, Pageable pageable);
		
	@Modifying
	@Query("UPDATE User u SET u.active = ?2 WHERE u.id = ?1")
	public void updateStatus(Long id, boolean status);
	
	@Query("SELECT u FROM User u WHERE u.role = 'manager'")
	public List<User> findAllManager();
	
	@Query("SELECT u FROM User u WHERE u.manageBy = ?1")
	public List<User> findAll(User manager);
	
	
	@Query("SELECT u FROM User u WHERE u.role = 'admin' ORDER BY u.id ASC")
	public List<User> findAllAdmin();
	
	@Query("UPDATE User u SET u.authenticationType = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateAuthenticationType(Long userId, AuthenticationType type);
	
	@Query("SELECT u FROM User u WHERE u.role = ?1")
	public List<User> findByRole(String role);
	
	@Query("SELECT u FROM User u WHERE u.role <> 'user'")
	public List<User> findByAdminManager();
	
}
