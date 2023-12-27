package com.projet.depouilledentaire.controllers;

import com.projet.depouilledentaire.entities.PW;
import com.projet.depouilledentaire.entities.Professor;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.PWRepository;
import com.projet.depouilledentaire.repositories.ToothRepository;
import com.projet.depouilledentaire.repositories.UserRepository;
import com.projet.depouilledentaire.services.PWService;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

@Controller
public class PWControllerWeb {

    @Autowired
    PWRepository pwRepository;

    @Autowired
    PWService pwService;

    @Autowired
    ToothRepository toothRepository;

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private String encodePhoto(byte[] photo) {
        return (photo != null && photo.length > 0) ? Base64.getEncoder().encodeToString(photo) : "";
    }

    private String encodeDoc(byte[] doc) {
        return (doc != null && doc.length > 0) ? Base64.getEncoder().encodeToString(doc) : "";
    }

    @GetMapping({ "/prof/pw" })
    public String showIndex(Model model, Principal principal) {
        User u = userRepository.findByUserName(principal.getName());
        List<PW> pws = pwRepository.findByProfessor((Professor)u);
        model.addAttribute("pws", pws);
         List<String> encodedDocs = new ArrayList<>();
        for (PW pw : pws) {
            encodedDocs.add(encodeDoc(pw.getDoc()));
        }
        model.addAttribute("encodedDocs", encodedDocs);
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        model.addAttribute("pw", new PW());
        model.addAttribute("mode", "");
        return "pw";
    }

    @GetMapping("/prof/pw/add")
    public String showAddForm(Model model, Principal principal) {
        User u = userRepository.findByUserName(principal.getName());
        model.addAttribute("pws", pwRepository.findByProfessor((Professor)u));
        model.addAttribute("pw", new PW());
        model.addAttribute("tooths", toothRepository.findAll());
        model.addAttribute("mode", "add");
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        return "pw";
    }

    @PostMapping("/prof/pw/addpw")
    public String addPW(@Validated PW pw, BindingResult result, Model model,
            @RequestParam("docFile") MultipartFile docFile) {
        if (result.hasErrors()) {
            return "pw";
        }
        try {
            if (docFile != null && !docFile.isEmpty()) {
                byte[] docBytes = docFile.getBytes();
                pw.setDoc(docBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(pw);
        pwRepository.save(pw);
        return "redirect:/prof/pw";
    }

    @GetMapping("/prof/pw/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model, Principal principal) {
        User u = userRepository.findByUserName(principal.getName());
        PW pw = pwRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pw Id:" + id));
        model.addAttribute("pws", pwRepository.findByProfessor((Professor)u));
        model.addAttribute("pw", pw);
        model.addAttribute("encodedDoc", encodeDoc(pw.getDoc()));
        model.addAttribute("tooths", toothRepository.findAll());
        model.addAttribute("mode", "update");
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        return "pw";
    }

    @PostMapping("/prof/pw/update/{id}")
    public String updatePW(@PathVariable("id") long id, @Validated PW pw, BindingResult result, Model model,
            @RequestParam(value = "docFile", required = false) MultipartFile docFile) {
        if (result.hasErrors()) {
            pw.setId(id);
            return "pw";
        }
        if (docFile != null && !docFile.isEmpty()) {
            try {
                byte[] docBytes = docFile.getBytes();
                pw.setDoc(docBytes);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            // If no new photo is provided, retain the existing photo
            PW existingPw = pwService.findById(id);
            if (existingPw != null) {
                pw.setDoc(existingPw.getDoc());
            }
        }

        pwService.create(pw);
        return "redirect:/prof/pw";
    }

    @GetMapping("/prof/pw/delete/{id}")
    public String deletePW(@PathVariable("id") long id, Model model) {
        PW pw = pwRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pw Id:" + id));
        // pwRepository.delete(pw);
        pwService.delete(pw);
        return "redirect:/prof/pw";
    }
}
