package com.coder.services;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

	public void remove_message_session() {
		
		System.out.println("Removing message");
		try {
			
		HttpSession session =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		session.removeAttribute("message");	
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
