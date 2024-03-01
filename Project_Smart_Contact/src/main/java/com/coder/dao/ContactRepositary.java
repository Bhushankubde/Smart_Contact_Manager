package com.coder.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coder.entities.ContactDetails;
import com.coder.entities.User;


public interface ContactRepositary extends JpaRepository<ContactDetails, Integer> {
   //pagination
	
	@Query("from ContactDetails as c where c.user.id =:userId")
	//current Page=page
	//contact per page=2
    public Page<ContactDetails> findContactByUser(@Param("userId")int userId,Pageable pageable);
	
	//search
	public List<ContactDetails> findByNameContainingAndUser(String name, User user);
}
