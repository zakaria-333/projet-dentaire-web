package com.projet.depouilledentaire.controllers;

import com.projet.depouilledentaire.entities.Groupe;
import com.projet.depouilledentaire.entities.Professor;
import com.projet.depouilledentaire.entities.Student;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.GroupeRepository;
import com.projet.depouilledentaire.repositories.StudentRepository;
import com.projet.depouilledentaire.repositories.UserRepository;
import com.projet.depouilledentaire.services.StudentService;
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
public class StudentControllerWeb {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    GroupeRepository groupeRepository;

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private String encodePhoto(byte[] photo) {
        return (photo != null && photo.length > 0) ? Base64.getEncoder().encodeToString(photo) : "";
    }

    @GetMapping("/prof/student")
    public String showIndex(Model model, Principal principal) {
        User u = userRepository.findByUserName(principal.getName());
        List<Student> students = studentRepository.findStudentsByProfessor(u.getId());
        List<String> encodedPhotos = new ArrayList<>();
        for (Student student : students) {
            encodedPhotos.add(encodePhoto(student.getPhoto()));
        }
        model.addAttribute("students", students);
        model.addAttribute("encodedPhotos", encodedPhotos);
        model.addAttribute("mode", "");
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        model.addAttribute("groupes", groupeRepository.findByProfessor((Professor)u));
        return "etudiant";
    }

    @GetMapping("/prof/student/{groupe}")
    public String show(Model model, Principal principal, @PathVariable("groupe") long gId) {
        User u = userRepository.findByUserName(principal.getName());
        List<Student> students = studentRepository.findByGroupe(groupeRepository.getById(gId));
        List<String> encodedPhotos = new ArrayList<>();
        for (Student student : students) {
            encodedPhotos.add(encodePhoto(student.getPhoto()));
        }
        model.addAttribute("students", students);
        model.addAttribute("encodedPhotos", encodedPhotos);
        model.addAttribute("mode", "");
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        model.addAttribute("groupes", groupeRepository.findByProfessor((Professor)u));
        return "etudiant";
    }

    @GetMapping("/prof/student/add")
    public String showAddForm(Model model, Principal principal) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("student", new Student());
        model.addAttribute("mode", "add");
        User u = userRepository.findByUserName(principal.getName());
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        model.addAttribute("groupes", groupeRepository.findByProfessor((Professor) u));
        return "etudiant";
    }

    @GetMapping("/prof/student/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model, Principal principal) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Student Id:" + id));
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("student", student);
        model.addAttribute("encodedPhoto", encodePhoto(student.getPhoto()));
        model.addAttribute("mode", "update");
        User u = userRepository.findByUserName(principal.getName());
        model.addAttribute("authenticatedProf", u);
        model.addAttribute("profImage", encodePhoto(u.getPhoto()));
        model.addAttribute("groupes", groupeRepository.findByProfessor((Professor) u));
        return "etudiant";
    }

    @GetMapping("/prof/student/delete/{id}")
    public String deleteStudent(@PathVariable("id") long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Student Id:" + id));
        studentService.delete(student);
        return "redirect:/prof/student";
    }

    @PostMapping("/prof/student/addstudent")
    public String addStudent(@Validated Student student, BindingResult result,
            @RequestParam("photoFile") MultipartFile photoFile, Model model) {
        if (result.hasErrors()) {
            return "etudiant";
        }
        try {
            if (photoFile != null && !photoFile.isEmpty()) {
                byte[] photoBytes = photoFile.getBytes();
                student.setPhoto(photoBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        student.setPassword(passwordEncoder.encode(student.getUserName()));
        studentService.create(student);
        return "redirect:/prof/student";
    }

    @PostMapping("/prof/student/update/{id}")
    public String updateStudent(
            @PathVariable("id") long id,
            @Validated Student student,
            BindingResult result,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
            Model model) {
        if (result.hasErrors()) {
            student.setId(id);
            return "etudiant";
        }

        // Check if a new photo is provided
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                byte[] photoBytes = photoFile.getBytes();
                student.setPhoto(photoBytes);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            // If no new photo is provided, retain the existing photo
            Student existingStudent = studentService.findById(id);
            if (existingStudent != null) {
                student.setPhoto(existingStudent.getPhoto());
            }
        }

        studentService.create(student);
        return "redirect:/prof/student";
    }
}
