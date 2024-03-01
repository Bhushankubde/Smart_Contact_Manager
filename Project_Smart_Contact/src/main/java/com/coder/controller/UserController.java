package com.coder.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.coder.dao.ContactRepositary;
import com.coder.dao.UserRepository;
import com.coder.entities.ContactDetails;
import com.coder.entities.User;
import com.coder.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepositary contactRepositary;
	
	@ModelAttribute
	public void commonDataHandler(Model model , Principal principal) {
		
		String userName = principal.getName();
		System.out.println("USERNAME:"+userName);
		
		User user = userRepository.getUserByName(userName);
		System.out.println(user);
		
		model.addAttribute("user",user);
		
		
	}
	
	@GetMapping("/index")
	public String customsigin(Model model,Principal principal) {
		
		model.addAttribute("title","User-DashBoard");
		return "normal/user_dashboard";
	}
	
	 //Add Contact Handler
	
	@GetMapping("/add-contact")
	public String addContacthandler(Model model) {
		
		
		model.addAttribute("title","Add-Contact");
		model.addAttribute("contact", new ContactDetails());
		return "normal/add_contact";
	}
	
	//processing add contact form
	
	@PostMapping("/details-contact")
	public String addProcessinghandler(
			@ModelAttribute ContactDetails contact,
			@RequestParam("profileimage")MultipartFile file,
			Principal principal, HttpSession session) {
		
		try {
		String name = principal.getName();
		
		User user = this.userRepository.getUserByName(name);
		
		/* contact me user save kara */
		contact.setUser(user);
		
		/* Fir user me contact ko save kara */
		user.getContact().add(contact);
		
		/* File ko save karna hai datbase me */
		
		if (file.isEmpty()) {
			//file me kuch nahi he
			System.out.println("File is empty!");
			contact.setImage("contact.png");			
		}else {
			
			//file me kuch hai toh file ko folder me aur contact me save karna hai
			   
			   contact.setImage(file.getOriginalFilename());
			
			   File savefile = new ClassPathResource("static/img").getFile();
			   
			  Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			   
			  Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			   
			   
			   
			   System.out.println("Image is uploaded!");
			
		}
		
		
		
		this.userRepository.save(user);
		
		System.out.println("DATA"+contact);
		
		System.out.println("Done");
		
		//success message
		session.setAttribute("message", new Message("You have sucessfully added contacts!!","success"));
		
	}catch (Exception e) {
		e.printStackTrace();
		
		//error message
	session.setAttribute("message", new Message("Try Again!Something went wrong..","danger"));

		
	}
		return "normal/add_contact";
		
	}
	
	
	//pagination ke two main points
		//kitne page chaiye ek page par=2
		//courrent page konsa hai =0 [page]
	
	@GetMapping("/show-contact/{page}")
	public String showContact(@PathVariable("page")Integer page, Model model, Principal principal) {
		
		model.addAttribute("title","Your-Contact");
		
		//contact list ko nikal ni hai
		
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByName(userName);
		
		//Pagination
		//currentpage =page
		//contact perpage = 2
		Pageable pageable = PageRequest.of(page, 2);
		
	    Page<ContactDetails> contacts = this.contactRepositary.findContactByUser(user.getId(),pageable);
	
	    model.addAttribute("contacts",contacts);
	    model.addAttribute("currentPage",page);
	    model.addAttribute("totalPages",contacts.getTotalPages());
	 
		
		return "normal/show_contact";
	}
	
	//specific contact details show
	@RequestMapping("/{cId}/contact")
	public String specificContactshow(@PathVariable("cId")Integer cId, Model model, Principal principal) {
		
		Optional<ContactDetails> optional = contactRepositary.findById(cId);
		ContactDetails contactDetails = optional.get();
		
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByName(userName);
		
		if (user.getId()==contactDetails.getUser().getId()) {
			
			model.addAttribute("title",contactDetails.getName());
			model.addAttribute("contact",contactDetails);
			
		}
		
		
		System.out.println("Cid"+cId);
		return "normal/specific_contact";
	}
	
	@GetMapping("/delete/{cid}")
	public String deletehandler(@PathVariable("cid") Integer cId, Model model, HttpSession session, Principal principal) {
		
		Optional<ContactDetails> optional = this.contactRepositary.findById(cId);
		
		ContactDetails contactDetails = optional.get();
		
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByName(userName);
		
		user.getContact().remove(contactDetails);
		
		this.userRepository.save(user);
		
		/*
		 * contactDetails.setUser(null); //check..
		 * this.contactRepositary.delete(contactDetails);
		 */
		
		
		session.setAttribute("message", new Message("Contact Delete successfully!","success"));
		
		return "redirect:/user/show-contact/0";
		
	}
	
	//update Profile handler
	
	@PostMapping("/update-contact/{cid}")
	public String updatehandler(@PathVariable("cid")Integer cId,Model model) {
		
		model.addAttribute("title","Update-Contact");
		
		ContactDetails contact = this.contactRepositary.findById(cId).get();
		
		model.addAttribute("contact",contact);
		
		
		
		return "normal/update_form";
	}
	
	//processing update
	
	@PostMapping("/details-update")
	public String updateprocessing(@ModelAttribute ContactDetails contactDetails,
			 Model model,
			HttpSession session, Principal principal) {
		try {
			
			String userName = principal.getName();
			 User user = this.userRepository.getUserByName(userName);
			 
			 //old contact details
			 ContactDetails oldcontact = this.contactRepositary.findById(contactDetails.getCid()).get();
			 
			 contactDetails.setImage(oldcontact.getImage());
			 
			 contactDetails.setUser(user);
			 
			 
			 
			 this.contactRepositary.save(contactDetails);
			 
			 session.setAttribute("message", new Message("Your contact is update!", "success"));
			
			System.out.println("Contact name:"+contactDetails.getName());
			System.out.println("Contact id:"+contactDetails.getCid());
			System.out.println(contactDetails.getImage());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/"+contactDetails.getCid()+"/contact";
	}
	
	//profile handler
	@GetMapping("/profile")
	public String profilehandler(Model model) {
		
		model.addAttribute("title","Profile Page");
		
		return "normal/user_profile";
	}
	
	//open settings handler
	
	@GetMapping("/settings")
	public String settinghandler(Model model) {
		
		
		model.addAttribute("title","Settings");
		return "normal/settings";
	}
	
	//settings password processing
	
	@PostMapping("/change-password")
	public String passwardhandler(@RequestParam("oldpassword")String oldpassword, @RequestParam("newpassword")
	String newpassword,Principal principal, HttpSession session) {
		
		System.out.println("Old:"+oldpassword);
		System.out.println("New:"+newpassword);
		
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByName(userName);
		System.out.println(user.getPassword());
		
		/* password matching process */
		
		if (this.bCryptPasswordEncoder.matches(oldpassword, user.getPassword())){
			//change password
			
			user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.userRepository.save(user);
			
			session.setAttribute("message", new Message("Your password successfully changed !!", "success"));
			
		}else {
			//error
			session.setAttribute("message", new Message("Please Enter your correct old Password!! ", "danger"));
			return "redirect:/user/settings";
		}
		
		return "redirect:/user/index";
	}
	
}
