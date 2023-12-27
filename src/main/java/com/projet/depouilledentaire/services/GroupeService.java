package com.projet.depouilledentaire.services;

import com.projet.depouilledentaire.dao.IDao;
import com.projet.depouilledentaire.entities.Groupe;
import com.projet.depouilledentaire.repositories.GroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupeService implements IDao<Groupe> {

    @Autowired
    private GroupeRepository groupeRepository;


    @Override
    public Groupe create(Groupe groupe) {
        groupeRepository.save(groupe);
        return groupe;
    }

    @Override
    public Groupe update(Groupe newgroupe) {
        return groupeRepository.save(newgroupe);
    }

    @Override
    public boolean delete(Groupe groupe) {
        try {


            groupeRepository.delete(groupe);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<Groupe> findAll() {
        return groupeRepository.findAll();
    }

    @Override
    public Groupe findById(Long id) {
        return groupeRepository.findById(id).orElse(null);
    }
}
