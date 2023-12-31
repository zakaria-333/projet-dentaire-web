package com.projet.depouilledentaire.services;

import com.projet.depouilledentaire.dao.IDao;
import com.projet.depouilledentaire.entities.PW;
import com.projet.depouilledentaire.entities.Student;
import com.projet.depouilledentaire.entities.StudentPW;
import com.projet.depouilledentaire.entities.StudentPWKey;
import com.projet.depouilledentaire.repositories.PWRepository;
import com.projet.depouilledentaire.repositories.StudentPWRepository;
import com.projet.depouilledentaire.repositories.StudentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentPWService implements IDao<StudentPW> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StudentPWRepository studentPWRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PWRepository pwRepository;

    @Override
    public StudentPW create(StudentPW studentPW) {
        studentPWRepository.save(studentPW);
        return studentPW;
    }

    @Override
    public StudentPW update(StudentPW studentPW) {
        studentPWRepository.save(studentPW);
        return studentPW;
    }

    @Override
    public boolean delete(StudentPW o) {
        return false;
    }

    public boolean delete(StudentPWKey id) {
        try {
            StudentPW ligneToDelete = studentPWRepository.findById(id).orElse(null);
            if (ligneToDelete != null) {
                // Récupération de la pharmacie et de la garde associées à la ligne
                Student student = ligneToDelete.getStudent();
                PW pw = ligneToDelete.getPw();

                // Suppression de la ligne de la liste dans la pharmacie et la garde
                if (student != null && pw != null) {
                    student.getStudentPWList().remove(ligneToDelete);
                    pw.getStudentPWList().remove(ligneToDelete);
                }

                // Suppression de la ligne
                studentPWRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<StudentPW> findAll() {
        return studentPWRepository.findAll();
    }

    @Override
    public StudentPW findById(Long id) {
        return null;
    }


    public StudentPW findById(StudentPWKey id) {
        Optional<StudentPW> optionalStudentPW = studentPWRepository.findById(id);
        return optionalStudentPW.orElse(null);
    }
}
