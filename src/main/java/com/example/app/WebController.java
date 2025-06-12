package com.example.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Java + Docker + Jenkins");
        model.addAttribute("message", "🚀 Your Stylish Spring Boot App is Live!");
        return "index";
    }
}
