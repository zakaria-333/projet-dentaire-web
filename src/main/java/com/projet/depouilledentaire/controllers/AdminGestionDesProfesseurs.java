package com.projet.depouilledentaire.controllers;

import com.projet.depouilledentaire.entities.Professor;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.ProfessorRepository;
import com.projet.depouilledentaire.repositories.UserRepository;
import com.projet.depouilledentaire.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class AdminGestionDesProfesseurs {

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

    @GetMapping("/admin/professor")
    public String showIndex(Model model, Principal principal) {
        List<Professor> professors = professorRepository.findAll();
        List<String> encodedPhotos = new ArrayList<>();
        for (Professor professor : professors) {
            encodedPhotos.add(encodePhoto(professor.getPhoto()));
        }
        model.addAttribute("professors", professors);
        model.addAttribute("encodedPhotos", encodedPhotos);
        model.addAttribute("mode", "");
        User u = userRepository.findByUserName(principal.getName());
        model.addAttribute("authenticatedAdmin", u);
        model.addAttribute("adminImage", encodePhoto(u.getPhoto()));
        return "admin";
    }

    @GetMapping("/admin/professor/add")
    public String showAddForm(Model model, Principal principal) {
        model.addAttribute("professors", professorRepository.findAll());
        model.addAttribute("professor", new Professor());
        model.addAttribute("mode", "add");
        User u = userRepository.findByUserName(principal.getName());
        model.addAttribute("authenticatedAdmin", u);
        model.addAttribute("adminImage", encodePhoto(u.getPhoto()));
        return "admin";
    }

    @GetMapping("/admin/professor/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model, Principal principal) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Professor Id:" + id));
        model.addAttribute("professors", professorRepository.findAll());
        model.addAttribute("professor", professor);
        model.addAttribute("encodedPhoto", encodePhoto(professor.getPhoto()));
        model.addAttribute("mode", "update");
        User u = userRepository.findByUserName(principal.getName());
        model.addAttribute("authenticatedAdmin", u);
        model.addAttribute("adminImage", encodePhoto(u.getPhoto()));
        return "admin";
    }

    @GetMapping("/admin/professor/delete/{id}")
    public String deleteProfessor(@PathVariable("id") long id, Model model) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Professor Id:" + id));
        professorService.delete(professor);
        return "redirect:/admin/professor";
    }

    @PostMapping("/admin/professor/addprofessor")
    public String addProfessor(@Validated Professor professor, BindingResult result,
            @RequestParam("photoFile") MultipartFile photoFile, Model model) {
        if (result.hasErrors()) {
            return "admin";
        }

        try {
            if (photoFile != null && !photoFile.isEmpty()) {
                byte[] photoBytes = photoFile.getBytes();
                professor.setPhoto(photoBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        professor.setPassword(passwordEncoder.encode(professor.getUserName()));
        professorService.create(professor);
        return "redirect:/admin/professor";
    }

    @PostMapping("/admin/professor/update/{id}")
    public String updateProfessor(
            @PathVariable("id") long id,
            @Validated Professor professor,
            BindingResult result,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
            Model model) {
        if (result.hasErrors()) {
            professor.setId(id);
            return "admin";
        }

        // Check if a new photo is provided
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                byte[] photoBytes = photoFile.getBytes();
                professor.setPhoto(photoBytes);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            // If no new photo is provided, retain the existing photo
            Professor existingProfessor = professorService.findById(id);
            if (existingProfessor != null) {
                professor.setPhoto(existingProfessor.getPhoto());
            }
        }

        professorService.create(professor);
        return "redirect:/admin/professor";
    }
}
