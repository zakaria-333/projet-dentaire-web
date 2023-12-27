package com.projet.depouilledentaire.services;

import com.projet.depouilledentaire.dao.IDao;
import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IDao<User> {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User create(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User update(User newuser) {
        return userRepository.save(newuser);
    }

    @Override
    public boolean delete(User user) {
        try {
            userRepository.delete(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
