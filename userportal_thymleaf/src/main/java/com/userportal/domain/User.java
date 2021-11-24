package com.userportal.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    private String firstName;
    private String lastName;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    
    @Column(unique = true)
    private String email;
    
    @Column(nullable = true)
    private String profileImage;
    
    private Date lastLoginDate;
    
    private String lastLoginDateDisplay;
    @CreationTimestamp
    private Date joinDate;
    
    private String role; //ROLE_USER{ read }, ROLE_ADMIN {read,edit,delete},ROLE_ADMIN{read,edit}
    
    private String[] authorities;
    
    private boolean active;
  
    @OneToMany
	private Set<User> manages = new HashSet<User>();
    
    
    public User() {
		// TODO Auto-generated constructor stub
	}


	public User(String firstName, String lastName, String username, String password, String email, String profileImage,
			Date lastLoginDate, String role, boolean active, Set<User> user) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.profileImage = profileImage;
		this.lastLoginDate = lastLoginDate;
		this.role = role;
		this.active = active;
		this.manages = user;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
		setLastLoginDateDisplay(lastLoginDate);
	}

	public String getLastLoginDateDisplay() {
		return lastLoginDateDisplay;
	}

	public void setLastLoginDateDisplay(Date lastLoginDateDisplay) {
		if(this.lastLoginDate != null) {
			this.lastLoginDateDisplay = this.lastLoginDate.toString();
		}
		
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String[] getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean isActive) {
		this.active = isActive;
	}


	

	public Set<User> getUser() {
		return manages;
	}


	public void setUser(Set<User> user) {
		this.manages = user;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + ", email=" + email + ", profileImage=" + profileImage + ", lastLoginDate="
				+ lastLoginDate + ", lastLoginDateDisplay=" + lastLoginDateDisplay + ", joinDate=" + joinDate
				+ ", role=" + role + ", authorities=" + Arrays.toString(authorities) + ", active=" + active + ", user="
				+ manages + "]";
	}

	@Transient
	public String getProfileImagePath() {
		if(this.id == null || this.profileImage == null) {
			return "/images/default-user.png";
		}
		else {
			return "/profile-image/" + this.id +"/" + this.profileImage;
		}
	}
	
}
