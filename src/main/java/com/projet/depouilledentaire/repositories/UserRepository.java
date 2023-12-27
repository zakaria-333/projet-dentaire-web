package com.projet.depouilledentaire.repositories;

import com.projet.depouilledentaire.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    User findByUserName(String username);
    User findByEmail(String email);
}


