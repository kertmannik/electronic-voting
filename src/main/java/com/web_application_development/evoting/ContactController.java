package com.web_application_development.evoting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {
    @RequestMapping(path = "/contact", method = RequestMethod.GET)
    public String getTestPage() {
        return "contact/index";
    }

}
