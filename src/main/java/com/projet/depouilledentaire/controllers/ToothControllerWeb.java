package com.projet.depouilledentaire.controllers;

import com.projet.depouilledentaire.entities.Tooth;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.ToothRepository;
import com.projet.depouilledentaire.repositories.UserRepository;

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


@Controller
public class ToothControllerWeb {

    @Autowired
    ToothRepository toothRepository;

    @Autowired
    UserRepository userRepository;
    
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private String encodePhoto(byte[] photo) {
        return (photo != null && photo.length > 0) ? Base64.getEncoder().encodeToString(photo) : "";
    }


    @GetMapping({"/prof/tooth"})
    public String showIndex(Model model, Principal principal){
        model.addAttribute("tooths",toothRepository.findAll());
        User u= userRepository.findByUserName(principal.getName());
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        model.addAttribute("tooth",new Tooth());
        model.addAttribute("mode","");
        return "tooth";
    }
    @GetMapping("/prof/tooth/add")
    public String showAddForm(Model model){
        model.addAttribute("tooths",toothRepository.findAll());
        model.addAttribute("tooth",new Tooth());
        model.addAttribute("mode","add");
        return "tooth";
    }
    @PostMapping("/prof/tooth/addtooth")
    public String addTooth(@Validated Tooth tooth, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "tooth";
        }
        toothRepository.save(tooth);
        return "redirect:/prof/tooth";
    }

    @GetMapping("/prof/tooth/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Tooth tooth = toothRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Invalid tooth Id:" + id));
        model.addAttribute("tooths",toothRepository.findAll());
        model.addAttribute("tooth", tooth);
        model.addAttribute("mode","update");
        return"tooth";
    }

    @PostMapping("/prof/tooth/update/{id}")
    public String updateTooth(@PathVariable("id") long id, @Validated Tooth tooth, BindingResult result, Model model) {
        if (result.hasErrors()) {
            tooth.setId(id);
            return"tooth";
        }
        toothRepository.save(tooth);
        return "redirect:/prof/tooth";
    }

    @GetMapping("/prof/tooth/delete/{id}")
    public String deleteTooth(@PathVariable("id") long id, Model model) {
        Tooth tooth = toothRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Invalid tooth Id:" + id));
        toothRepository.delete(tooth);
        return "redirect:/prof/tooth";
    }
}
