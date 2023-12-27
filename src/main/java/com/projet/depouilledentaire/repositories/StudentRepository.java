package com.projet.depouilledentaire.repositories;

import com.projet.depouilledentaire.entities.Groupe;
import com.projet.depouilledentaire.entities.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
  @Query("SELECT s FROM Student s JOIN s.groupe g JOIN g.professor p WHERE p.id = :professorId")
  List<Student> findStudentsByProfessor(@Param("professorId") Long professorId);

  List<Student> findByGroupe(Groupe groupe);
}
