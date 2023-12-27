package com.projet.depouilledentaire.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.projet.depouilledentaire.entities.Professor;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.ProfessorRepository;
import com.projet.depouilledentaire.repositories.UserRepository;
import com.projet.depouilledentaire.services.ProfessorService;

@Controller
public class ProfilContoller {
  @Autowired
  ProfessorRepository professorRepository;

  @Autowired
  ProfessorService professorService;

  @Autowired
  UserRepository userRepository;

  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private String encodePhoto(byte[] photo) {
    return (photo != null && photo.length > 0) ? Base64.getEncoder().encodeToString(photo) : "";
  }

  @GetMapping("/prof/profil")
  public String showProfil(Model model, Principal principal) {
    User u = userRepository.findByUserName(principal.getName());
    model.addAttribute("authenticatedProf", u);
    model.addAttribute("profImage", encodePhoto(u.getPhoto()));
    model.addAttribute("professor", new Professor());
    return "profil";
  }

  @PostMapping("/prof/profil/update/{id}")
  public String updateProfessor(@PathVariable("id") long id, @Validated Professor professor, BindingResult result,
      @RequestParam("photoFile") MultipartFile photoFile, Model model) {
    if (result.hasErrors()) {
      professor.setId(id);
      return "admin";
    }

    try {
      if (photoFile != null && !photoFile.isEmpty()) {
        byte[] photoBytes = photoFile.getBytes();
        professor.setPhoto(photoBytes);
      }
    } catch (IOException e) {
      e.printStackTrace(); // Gérer l'exception correctement
    }
    professor.setPassword(passwordEncoder.encode(professor.getPassword()));
    professorService.create(professor);
    return "redirect:/logout";
  }
}
