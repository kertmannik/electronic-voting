package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.ContactDTO;
import com.web_application_development.evoting.services.UserStatisticsService;
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
import javax.servlet.http.HttpServletRequest;

@Controller
public class ContactController {

    final String DEVELOPER_EMAIL = "kert.mannik@gmail.com";
    final String SUBJECT = "E-voting / E-hääletamine ";
    final String BODY = "Hello!" + "\n" + "\n" + "Thank you for contacting us! We will answer you shortly." + "\n" + "\n" + "E-voting developers"
            + "\n" + "\n" + "---------------" + "\n" + "\n" +
            "Tervist!" + "\n" + "\n" + "Täname teid, et meiega ühendust võtsite! Vastame teile peagi." + "\n" + "\n" + "E-hääletamise arendajad";
    @Autowired
    public JavaMailSender emailsender;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserStatisticsService userStatisticsService;

    @RequestMapping(path = "/contact", method = RequestMethod.GET)
    public String getTestPage() {
        userStatisticsService.saveUserStatistics(request, "/contact");
        return "contact/index";
    }

    @PostMapping(path = "/contact")
    public String sendEmailNotification(@ModelAttribute ContactDTO contactDTO) {
        userStatisticsService.saveUserStatistics(request, "/contact");
        String name = contactDTO.getName();
        String mail = contactDTO.getEmail();
        String subject = contactDTO.getSubject();
        String body = contactDTO.getBody();

        createAndSendEmail(mail, SUBJECT, BODY);
        createAndSendEmail(DEVELOPER_EMAIL, subject, body);
        return "redirect:/";
    }

    private void createAndSendEmail(String email, String subject, String body) {
        MimeMessage message = emailsender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(email);
            helper.setText(body, false);
            helper.setSubject(subject);
            emailsender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
