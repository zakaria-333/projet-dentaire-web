package com.projet.depouilledentaire.repositories;

import com.projet.depouilledentaire.entities.Student;
import com.projet.depouilledentaire.entities.StudentPW;
import com.projet.depouilledentaire.entities.StudentPWKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface StudentPWRepository extends JpaRepository<StudentPW, StudentPWKey> {
  List<StudentPW> findByStudent(Student student);
}
