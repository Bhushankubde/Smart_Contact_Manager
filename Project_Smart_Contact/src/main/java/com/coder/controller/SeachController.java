package com.coder.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.coder.dao.ContactRepositary;
import com.coder.dao.UserRepository;
import com.coder.entities.ContactDetails;
import com.coder.entities.User;

@RestController
public class SeachController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepositary contactRepositary;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query,Principal principal) {
		
		System.out.println(query);
		
	
		User user = this.userRepository.getUserByName(principal.getName());
		
		List<ContactDetails> contacts = this.contactRepositary.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
		
		
	}
	
}
