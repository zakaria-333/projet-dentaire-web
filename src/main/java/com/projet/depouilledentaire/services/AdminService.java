package com.projet.depouilledentaire.services;

import com.projet.depouilledentaire.dao.IDao;
import com.projet.depouilledentaire.entities.Admin;
import com.projet.depouilledentaire.entities.Role;
import com.projet.depouilledentaire.repositories.AdminRepository;
import com.projet.depouilledentaire.repositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IDao<Admin> {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired 
    private RoleRepository roleRepository;

    


    @Override
    public Admin create(Admin admin) {
        Role adminRole = roleRepository.getById(1L);
        admin.setRole(adminRole);
        adminRepository.save(admin);
        return admin;
    }

    @Override
    public Admin update(Admin newadmin) {
        return adminRepository.save(newadmin);
    }

    @Override
    public boolean delete(Admin admin) {
        try {
            adminRepository.delete(admin);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin findById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }
}
