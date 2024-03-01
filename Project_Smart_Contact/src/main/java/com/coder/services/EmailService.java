package com.coder.services;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class EmailService {

	public boolean sendEmail(String subject , String message, String to)
    {

         boolean flage= false;

        String from="email id";

        String host ="smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        //Step-1 session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("kud ki email","*********");
            }
        });

        session.setDebug(true);
        //Step-2 compose the message
       MimeMessage m = new MimeMessage(session);
       try{
           m.setFrom(from);
           m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
           m.setSubject(subject);
//           m.setText(message);
           m.setContent(message,"text/html");

           //send the message
           Transport.send(m);
           System.out.println("Message successfully sent");
           flage =true;
       }catch (Exception e){
           e.printStackTrace();
       }
       return flage;
    }
}
