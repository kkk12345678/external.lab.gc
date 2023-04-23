package org.example.gs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
    @RequestMapping("/400")
    public String badRequest(@ModelAttribute("message") Object flashAttribute, ModelMap model) {
        model.addAttribute("message", flashAttribute);
        return "400";
    }
}
