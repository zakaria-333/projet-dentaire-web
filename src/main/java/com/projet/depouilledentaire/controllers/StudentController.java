package com.projet.depouilledentaire.controllers;

import com.projet.depouilledentaire.entities.*;
import com.projet.depouilledentaire.repositories.StudentPWRepository;
import com.projet.depouilledentaire.repositories.StudentRepository;
import com.projet.depouilledentaire.repositories.UserRepository;
import com.projet.depouilledentaire.services.PWService;
import com.projet.depouilledentaire.services.StudentPWService;
import com.projet.depouilledentaire.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentPWService studentPWService;




    @GetMapping("")
    public List<Student> getAllStudent() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable Long id, @RequestBody Student newstudent) {
       Student oldstudent = studentService.findById(id);
        if (oldstudent == null) {
            return new ResponseEntity<Object>("Student avec ID " + id + " n'existe pas", HttpStatus.BAD_REQUEST);
        } else {

            newstudent.setId(id);
            newstudent.setPhoto(newstudent.getPhoto());
            //newstudent.setStudentPWList(oldstudent.getStudentPWList());
            newstudent.setGroup(oldstudent.getGroup());
            newstudent.setRole(oldstudent.getRole());
            newstudent.setPassword(oldstudent.getPassword());
            newstudent.setNumber(newstudent.getNumber());
            newstudent.setEmail(newstudent.getEmail());
            newstudent.setFirstName(newstudent.getFirstName());
            newstudent.setLastName(newstudent.getLastName());
            newstudent.setUserName(newstudent.getUserName());

            studentService.update(newstudent);
            return ResponseEntity.ok("MISE À JOUR AVEC SUCCÈS");
        }
    }



    @PostMapping("/login")
    public ResponseEntity<Object> loginStudent(@RequestBody User loginuser){
        User user = userRepository.findByEmail(loginuser.getEmail());
        if (user == null)
            return new ResponseEntity<>(Map.of("message","invalid email or password"),HttpStatus.NOT_FOUND);
        else{
            boolean isRegister = BCrypt.checkpw(loginuser.getPassword(), user.getPassword());
            if(!isRegister)
                return new ResponseEntity<>(Map.of("message","invalid email or password"),HttpStatus.NOT_FOUND);
            else{
                if (user instanceof Student){
                    return ResponseEntity.ok(user);

                }else{
                    return new ResponseEntity<>(Map.of("message","invalid email or password"),HttpStatus.NOT_FOUND);
                }

            }
        }
    }


    @GetMapping("/pw/{id}")
    public ResponseEntity<?> getStudentPw(@PathVariable long id){
        User user =  studentService.findById(id);
        if(user == null)
            return new ResponseEntity<>(Map.of("message","User does not exist"), HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(studentRepository.findPwByStudent(id));
    }
    


    @Autowired
    PWService pwService;

    @PostMapping("/add/{studentId}/{pwId}")
    public ResponseEntity<Object> create(
            @PathVariable long studentId,
            @PathVariable long pwId,
            @RequestBody StudentPW studentPW
    ) {
        // Récupérer l'étudiant et le PW à partir de leurs IDs
        Student student = studentService.findById(studentId);
        PW pw = pwService.findById(pwId);

        // Vérifier si l'étudiant et le PW existent
        if (student == null || pw == null) {
            // Gérer le cas où l'étudiant ou le PW n'existent pas
            return ResponseEntity.badRequest().body("Étudiant ou PW introuvable.");
        }

        // Initialiser la clé primaire composite
        StudentPWKey studentPWPK = new StudentPWKey(studentId, pwId);
        studentPW.setId(studentPWPK);

        // Assigner l'objet Student et PW à l'objet StudentPW
        studentPW.setStudent(student);
        studentPW.setPw(pw);

        // Enregistrer l'entité StudentPW
        studentPWService.create(studentPW);

        return ResponseEntity.ok(studentPW);
    }


    @PostMapping("/student/{studentId}/pw/{pwId}")
    public ResponseEntity<String> createStudentPW(@PathVariable Long studentId, @PathVariable Long pwId) {
        try {
            // Vous pouvez implémenter une méthode dans le service pour gérer la création de StudentPW
            studentPWService.createRelationByIDs(studentId, pwId);
            return new ResponseEntity<>("StudentPW créé avec succès pour Student avec l'ID " + studentId + " et PW avec l'ID " + pwId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la création de StudentPW: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}