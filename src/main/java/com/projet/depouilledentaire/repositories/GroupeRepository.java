package com.projet.depouilledentaire.repositories;

import com.projet.depouilledentaire.entities.Groupe;
import com.projet.depouilledentaire.entities.Professor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface GroupeRepository extends JpaRepository<Groupe,Long> {
  List<Groupe> findByProfessor(Professor professor);
}
