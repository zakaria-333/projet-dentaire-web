package com.projet.depouilledentaire.repositories;

import com.projet.depouilledentaire.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
}

