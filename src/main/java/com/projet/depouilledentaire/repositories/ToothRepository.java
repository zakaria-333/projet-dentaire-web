package com.projet.depouilledentaire.repositories;


import com.projet.depouilledentaire.entities.Tooth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToothRepository extends JpaRepository<Tooth,Long> {
}
