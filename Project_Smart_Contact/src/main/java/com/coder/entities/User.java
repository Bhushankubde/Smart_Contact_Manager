package com.coder.entities;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.tools.Trace;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(
		    name="course_seq",
		    sequenceName="course_sequence",
		    allocationSize=1
		)
	private int id;
     
	@NotBlank(message = "Username cannot be empty!!")
    @Size(min = 3,max = 12,message = "Username must be between 3 to 12 characters!!")
	private String name;
	
	@Column(unique = true)
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid Email!!")
	private String email;
	private String password;
	private String imageUrl;
	@Column(length = 400)
	private String about;
	private String role;
	private boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user",orphanRemoval = true)
	List<ContactDetails> contact = new ArrayList<>();
	
	public User() {
	
	}
	
	public User(int id, String name, String email, String password, String imageUrl, String about, String role,
			boolean enabled) {
		
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.imageUrl = imageUrl;
		this.about = about;
		this.role = role;
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public List<ContactDetails> getContact() {
		return contact;
	}

	public void setContact(List<ContactDetails> contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", imageUrl="
				+ imageUrl + ", about=" + about + ", role=" + role + ", enabled=" + enabled + "]";
	}
	
	
	
	
}