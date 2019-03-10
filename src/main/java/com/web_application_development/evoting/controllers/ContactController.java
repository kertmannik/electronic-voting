package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.ContactDTO;
import com.web_application_development.evoting.services.UserStatisticsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Controller
public class ContactController {

    private static final Logger logger = LogManager.getLogger(ContactController.class);

    private final String DEVELOPER_EMAIL = "kert.mannik@gmail.com";

    private final JavaMailSender mailSender;
    private final HttpServletRequest request;
    private final UserStatisticsService userStatisticsService;
    private final MessageSource messageSource;

    @Autowired
    public ContactController(JavaMailSender emailSender, HttpServletRequest request, UserStatisticsService userStatisticsService, MessageSource messageSource) {
        this.mailSender = emailSender;
        this.request = request;
        this.userStatisticsService = userStatisticsService;
        this.messageSource = messageSource;
    }

    @RequestMapping(path = "/contact", method = RequestMethod.GET)
    public String getTestPage() {
        logger.debug("Contact page GET request");
        userStatisticsService.saveUserStatistics(request, "/contact");
        return "contact/index";
    }

    @PostMapping(path = "/contact")
    public String sendEmailNotification(@ModelAttribute ContactDTO contactDTO, Model model) {
        try {
            userStatisticsService.saveUserStatistics(request, "/contact");
            String userName = contactDTO.getName();
            String userMail = contactDTO.getEmail();
            String userSubject = contactDTO.getSubject();
            String userBody = contactDTO.getBody();

            logger.debug("Contact page E-mail sending from " + userName + " " + userMail);

            String message = messageSource.getMessage("email.lettersent", Collections.emptyList().toArray(), LocaleContextHolder.getLocale());
            String subject = messageSource.getMessage("email.title", Collections.emptyList().toArray(), LocaleContextHolder.getLocale());

            createAndSendEmail(userMail, subject, message);
            createAndSendEmail(DEVELOPER_EMAIL, userSubject, userBody);
            model.addAttribute("contactSuccessMessage", messageSource.getMessage("error.contactsuccess", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } catch (Exception exception) {
            model.addAttribute("contactErrorMessage", messageSource.getMessage("error.contacterror", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        }
        return "contact/index";
    }

    private void createAndSendEmail(String email, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(email);
            helper.setText(body, false);
            helper.setSubject(subject);
            mailSender.send(message);
            logger.debug("E-mail sent SUCCESSFULLY to " + email);
        } catch (Exception e) {
            logger.error("E-mail sent ERROR when sending to " + email);
            e.printStackTrace();
        }
    }

}
