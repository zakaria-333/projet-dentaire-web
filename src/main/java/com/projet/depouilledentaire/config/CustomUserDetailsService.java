package com.projet.depouilledentaire.config;


import com.projet.depouilledentaire.entities.User;
import com.projet.depouilledentaire.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username or Password not found");
        }

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                user.getLastName(),
                user.getRole(),
                user.getFirstName());

        // Ajoutez des logs pour vérifier les détails d'authentification
        System.out.println("UserDetails: " + userDetails);
        return userDetails;
    }
}
