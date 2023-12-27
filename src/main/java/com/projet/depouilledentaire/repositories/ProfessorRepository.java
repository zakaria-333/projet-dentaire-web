package com.projet.depouilledentaire.repositories;

import com.projet.depouilledentaire.entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor,Long> {
}
