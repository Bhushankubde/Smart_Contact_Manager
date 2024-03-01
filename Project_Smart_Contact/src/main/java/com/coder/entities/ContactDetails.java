package com.coder.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact")
public class ContactDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(
		    name="course_seq",
		    sequenceName="course_sequence",
		    allocationSize=1
		)
	private int cid;
	private String name;
	@Column(unique = true)
	private String email;
	private String nickName;
	private String work;
	private String phone;
	private String image;
	@Column(length = 1000)
	private String description;
	
	@ManyToOne
	@JsonIgnore
    private User user;
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "ContactDetails [cid=" + cid + ", name=" + name + ", email=" + email + ", nickName=" + nickName
				+ ", work=" + work + ", phone=" + phone + ", image=" + image + ", description=" + description
				+ ", user=" + user + "]";
	}
	@Override
	public boolean equals(Object obj) {
		
		return this.cid==((ContactDetails)obj).getCid();
	}
	
	
	
}
