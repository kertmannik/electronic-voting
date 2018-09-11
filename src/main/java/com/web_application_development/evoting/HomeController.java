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
            @RequestParam(value = "name",
                    required = false,
                    defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "home/index_dynamic";
    }
}
