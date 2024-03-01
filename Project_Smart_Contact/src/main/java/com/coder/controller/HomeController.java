package com.coder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coder.dao.UserRepository;
import com.coder.entities.User;
import com.coder.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
    private UserRepository userRepository;
	
	@RequestMapping("/")
	public String Homehandler(Model model) {
		
		model.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String abouthandler(Model model) {
		
		model.addAttribute("title","About-Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signuphandler(Model model) {
		
		model.addAttribute("title","Signup-Smart Copntact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String registerHnadler(@Valid @ModelAttribute("user") User user ,BindingResult result, @RequestParam(value = "agreement",defaultValue = "false")Boolean agreement, Model model,
			HttpSession session) {
		
		try {
            
			if (result.hasErrors()) {
				
				System.out.println(result);
				
				  model.addAttribute("user",user);
				 			
				return "signup";
			}
			
			if (!agreement) {
				System.out.println("You have agreed the terms and conditons");
				throw new Exception("You have agreed the terms and conditons");
			}
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User result1 = this.userRepository.save(user);
			System.out.println("Agreement"+agreement);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registered!!","alert-success"));
			return "signup";

			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	
		
	}
	
	@RequestMapping("/signin")
	public String signin(Model model) {
		
		model.addAttribute("title","Sigin-Smart Copntact Manager");
		return "login";
	}

	
  }
