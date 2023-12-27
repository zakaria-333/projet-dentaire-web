package com.projet.depouilledentaire.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet.depouilledentaire.entities.Groupe;
import com.projet.depouilledentaire.entities.PW;
import com.projet.depouilledentaire.entities.Professor;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.GroupeRepository;
import com.projet.depouilledentaire.repositories.PWRepository;
import com.projet.depouilledentaire.repositories.ProfessorRepository;
import com.projet.depouilledentaire.repositories.UserRepository;
import com.projet.depouilledentaire.services.GroupeService;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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
import com.fasterxml.jackson.core.type.TypeReference;

@Controller
public class GroupeControllerWeb {

    @Autowired
    GroupeRepository groupeRepository;

    @Autowired
    GroupeService groupeService;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PWRepository pwRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private String encodePhoto(byte[] photo) {
        return (photo != null && photo.length > 0) ? Base64.getEncoder().encodeToString(photo) : "";
    }

    @GetMapping("/prof/groupe")
    public String showIndex(Model model, Principal principal) {
        User u = userRepository.findByUserName(principal.getName());
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        model.addAttribute("groupes", groupeRepository.findByProfessor((Professor) u));
        model.addAttribute("groupe", new Groupe());
        model.addAttribute("mode", "");
        return "groupe";
    }

    @GetMapping("/prof/groupe/add")
    public String showAddForm(Model model, Principal principal) {
        User u = userRepository.findByUserName(principal.getName());
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        model.addAttribute("groupes", groupeRepository.findByProfessor((Professor) u));
        model.addAttribute("groupe", new Groupe());
        model.addAttribute("professors", professorRepository.findAll());
        model.addAttribute("mode", "add");
        model.addAttribute("pws", pwRepository.findByProfessor((Professor) u));
        return "groupe";
    }

    @PostMapping("/prof/groupe/addgroupe")
    public String addGroupe(@Validated Groupe groupe, BindingResult result, Model model,
            @RequestParam(name = "dabab") String dababValues) {
        if (result.hasErrors()) {
            return "groupe";
        }

        try {
            // Utilisez Jackson pour désérialiser la chaîne JSON en une liste de chaînes
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> stringValues = objectMapper.readValue(dababValues, new TypeReference<List<String>>() {
            });

            // Convertissez les chaînes en nombres
            List<Long> ids = stringValues.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            // Récupérez les objets PW en utilisant les identifiants
            List<PW> pws = ids.stream()
                    .map(id -> pwRepository.getById(id))
                    .collect(Collectors.toList());

            groupe.setPws(pws);

            // Continuez avec le reste de votre logique
            groupeRepository.save(groupe);

        } catch (IOException e) {
            // Gérez l'exception en cas d'erreur de désérialisation ou de conversion
            e.printStackTrace();
        }

        return "redirect:/prof/groupe";
    }

    @GetMapping("/prof/groupe/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model, Principal principal) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid groupe Id:" + id));
        User u = userRepository.findByUserName(principal.getName());
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        model.addAttribute("groupes", groupeRepository.findByProfessor((Professor) u));
        model.addAttribute("groupe", groupe);
        model.addAttribute("professors", professorRepository.findAll());
        model.addAttribute("mode", "update");
        model.addAttribute("pws", pwRepository.findByProfessor((Professor) u));
        return "groupe";
    }

    @PostMapping("/prof/groupe/update/{id}")
    public String updateGroupe(@PathVariable("id") long id, @Validated Groupe groupe, BindingResult result,
            Model model, @RequestParam(name = "dabab") String dababValues) {
        if (result.hasErrors()) {
            groupe.setId(id);
            return "groupe";
        }
        try {
            // Utilisez Jackson pour désérialiser la chaîne JSON en une liste de chaînes
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> stringValues = objectMapper.readValue(dababValues, new TypeReference<List<String>>() {
            });

            // Convertissez les chaînes en nombres
            List<Long> ids = stringValues.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            // Récupérez les objets PW en utilisant les identifiants
            List<PW> pws = ids.stream()
                    .map(idd -> pwRepository.getById(idd))
                    .collect(Collectors.toList());

            groupe.setPws(pws);

            // Continuez avec le reste de votre logique
            groupeRepository.save(groupe);

        } catch (IOException e) {
            // Gérez l'exception en cas d'erreur de désérialisation ou de conversion
            e.printStackTrace();
        }

        groupeRepository.save(groupe);
        return "redirect:/prof/groupe";
    }

    @GetMapping("/prof/groupe/delete/{id}")
    public String deleteGroupe(@PathVariable("id") long id, Model model) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid groupe Id:" + id));
        groupeService.delete(groupe);
        return "redirect:/prof/groupe";
    }
}
