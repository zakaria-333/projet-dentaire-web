package com.projet.depouilledentaire.repositories;


import com.projet.depouilledentaire.entities.PW;
import com.projet.depouilledentaire.entities.Professor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PWRepository extends JpaRepository<PW,Long> {
  List<PW> findByProfessor(Professor professor);
}
