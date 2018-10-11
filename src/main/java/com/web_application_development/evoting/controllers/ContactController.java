package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.ContactDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Controller
public class ContactController {

    @Autowired
    public JavaMailSender emailsender;

    @RequestMapping(path = "/contact", method = RequestMethod.GET)
    public String getTestPage() {
        return "contact/index";
    }

    @PostMapping(path = "/contact")
    public String sendEmailNotification(@ModelAttribute ContactDTO contactDTO) {
        String name = contactDTO.getName();
        String mail = contactDTO.getEmail();
        String subject = contactDTO.getSubject();
        String body = contactDTO.getBody();

        System.out.println(name + mail + subject + body);

        MimeMessage message = emailsender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(mail);
            helper.setText(body);
            helper.setSubject(subject);
            emailsender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        /*SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mail);
        msg.setText(body);
        msg.setSubject(subject);
        emailsender.send(msg);*/
        return "redirect:/";
    }

}
