package com.web_application_development.evoting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String getTestPage() {
        return "home/index";
    }

    @GetMapping(path = "/test-dynamic")
    public String getTest2Page(
            @RequestParam(value = "name-from-url",
                    required = false,
                    defaultValue = "World") String nameFromUrl, Model model) {
        model.addAttribute("nameTemplate", nameFromUrl);
        return "home/index_dynamic";
    }
}
