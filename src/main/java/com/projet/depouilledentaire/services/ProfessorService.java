package com.projet.depouilledentaire.services;

import com.projet.depouilledentaire.dao.IDao;
import com.projet.depouilledentaire.entities.Groupe;
import com.projet.depouilledentaire.entities.Professor;
import com.projet.depouilledentaire.entities.Role;
import com.projet.depouilledentaire.repositories.ProfessorRepository;
import com.projet.depouilledentaire.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProfessorService implements IDao<Professor> {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired 
    private RoleRepository roleRepository;

    

    @Override
    public Professor create(Professor professor) {
        Role profRole = roleRepository.getById(3L);
        professor.setRole(profRole);
        professorRepository.save(professor);
        return professor;
    }

    @Override
    public Professor update(Professor newprofessor) {
        return professorRepository.save(newprofessor);
    }

    @Override
    public boolean delete(Professor professor) {
        try {

            List<Groupe> grps = professorRepository.findById(professor.getId()).get().getGroups();
            for(Groupe g : grps){
                g.setProfessor(null);
            }

            professorRepository.delete(professor);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    @Override
    public Professor findById(Long id) {
        return professorRepository.findById(id).orElse(null);
    }
}

