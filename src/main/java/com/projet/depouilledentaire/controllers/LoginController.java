package com.projet.depouilledentaire.controllers;

import com.projet.depouilledentaire.config.CustomUserDetails;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    @Autowired
    UserRepository userRepository;
    

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    @GetMapping("/login")
    public String login(Model model, User user, @RequestParam(name = "error", required = false) String error) {
        model.addAttribute("user", user);
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "loginPage";
    }

    @GetMapping("/check")
    public String role(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = false;

        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            isAdmin = true;
        }

        if (isAdmin) {
            return "redirect:/admin/professor";
        } else {
            return "redirect:/prof/groupe";
        }
    }
}
