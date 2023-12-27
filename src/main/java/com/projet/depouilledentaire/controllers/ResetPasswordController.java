package com.projet.depouilledentaire.controllers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.UserRepository;
import com.projet.depouilledentaire.services.EmailSender;

@Controller
public class ResetPasswordController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailSender emailSender;

  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @GetMapping("/resetPassword")
  public String show(Model model) {
    return "forgotPassword";
  }

  @PostMapping("/resetPassword")
  public String validateEmail(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
    User user = userRepository.findByEmail(email);
    if (user != null) {
      String code = generateRandomCode();
      emailSender.sendEmail(email, "Code d'activation pour la recuperation le mot de passe", "Votre Code est: " + code);
      redirectAttributes.addFlashAttribute("code", code);
      redirectAttributes.addFlashAttribute("email", email);
    } else {
      redirectAttributes.addFlashAttribute("error", "Invalid email");
    }
    return "redirect:/resetPassword";
  }

  @PostMapping("/resetPassword/new")
  public String changePassword(@RequestParam("email") String email, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
    User user = userRepository.findByEmail(email);
    if (user != null) {
      user.setPassword(passwordEncoder.encode(password));
      userRepository.save(user);
      redirectAttributes.addFlashAttribute("success", "Password reset successfully.");
      return "redirect:/login";
    } else {
      return "redirect:/resetPassword?error=UserNotFound";
    }
  }
  private String generateRandomCode() {
    String characters = "0123456789";
    Random random = new Random();
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      int index = random.nextInt(characters.length());
      code.append(characters.charAt(index));
    }
    return code.toString();
  }
}
