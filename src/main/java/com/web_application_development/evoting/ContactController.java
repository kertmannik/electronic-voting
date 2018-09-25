package com.web_application_development.evoting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ContactController {
    @RequestMapping(path = "/contact", method = RequestMethod.GET)
    public String getTestPage() {
        return "contact/index";
    }

}
