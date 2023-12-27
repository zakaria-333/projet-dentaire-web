package com.projet.depouilledentaire.services;

import com.projet.depouilledentaire.dao.IDao;
import com.projet.depouilledentaire.entities.Tooth;
import com.projet.depouilledentaire.repositories.ToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToothService implements IDao<Tooth> {

    @Autowired
    private ToothRepository toothRepository;


    @Override
    public Tooth create(Tooth tooth) {
        toothRepository.save(tooth);
        return tooth;
    }

    @Override
    public Tooth update(Tooth newtooth) {
        return toothRepository.save(newtooth);
    }

    @Override
    public boolean delete(Tooth tooth) {
        try {

            toothRepository.delete(tooth);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<Tooth> findAll() {
        return toothRepository.findAll();
    }

    @Override
    public Tooth findById(Long id) {
        return toothRepository.findById(id).orElse(null);
    }
}
