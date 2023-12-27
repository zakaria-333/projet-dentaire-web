package com.projet.depouilledentaire.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.projet.depouilledentaire.entities.Student;
import com.projet.depouilledentaire.entities.StudentPW;
import com.projet.depouilledentaire.entities.StudentPWKey;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.StudentPWRepository;
import com.projet.depouilledentaire.repositories.StudentRepository;
import com.projet.depouilledentaire.repositories.UserRepository;

@Controller
public class pwStudentController {

  @Autowired
  StudentPWRepository studentPWRepository;
  @Autowired
  StudentRepository studentRepository;
  @Autowired
  UserRepository userRepository;

  private String encodePhoto(byte[] photo) {
    return (photo != null && photo.length > 0) ? Base64.getEncoder().encodeToString(photo) : "";
  }


  @GetMapping("/prof/student/pws/{id}")
  public String showTps(Model model, Principal principal, @PathVariable("id") long idStudent) {
    User u = userRepository.findByUserName(principal.getName());
    model.addAttribute("authenticatedProf", u);
    model.addAttribute("profImage", encodePhoto(u.getPhoto()));
    model.addAttribute("student", studentRepository.getById(idStudent));
    List<StudentPW> pws = studentPWRepository.findByStudent(studentRepository.getById(idStudent));
    List<String> encodedPhotos = new ArrayList<>();
    for (StudentPW pw : pws) {
      encodedPhotos.add(encodePhoto(pw.getImageFront()));
    }
    model.addAttribute("pws", pws);
    model.addAttribute("encodedPhotos", encodedPhotos);
    return "pwStudent";
  }

  @GetMapping("/prof/student/pws/{note}/{idStudent}/{idPw}")
  public String validatenote(Model model, Principal principal, @PathVariable("idStudent") long idStudent,
      @PathVariable("idPw") long idPw,
      @PathVariable("note") float note) {
        StudentPWKey sid = new StudentPWKey(idStudent, idPw);
        StudentPW pw = studentPWRepository.getById(sid);
        pw.setNote(note);
        studentPWRepository.save(pw);
    return "redirect:/prof/student/pws/" +idStudent;
  }

}
