package com.projet.depouilledentaire.services;

import com.projet.depouilledentaire.dao.IDao;
import com.projet.depouilledentaire.entities.Role;
import com.projet.depouilledentaire.entities.Student;
import com.projet.depouilledentaire.repositories.RoleRepository;
import com.projet.depouilledentaire.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IDao<Student> {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired 
    private RoleRepository roleRepository;

    


    @Override
    public Student create(Student student) {
        Role studentRole = roleRepository.getById(2L);
        student.setRole(studentRole);
        studentRepository.save(student);
        return student;
    }

    @Override
    public Student update(Student newstudent) {
        return studentRepository.save(newstudent);
    }

    @Override
    public boolean delete(Student student) {
        try {
            studentRepository.delete(student);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
}
