package com.projet.depouilledentaire.services;


import com.projet.depouilledentaire.dao.IDao;
import com.projet.depouilledentaire.entities.PW;
import com.projet.depouilledentaire.entities.StudentPW;
import com.projet.depouilledentaire.entities.StudentPWKey;
import com.projet.depouilledentaire.repositories.PWRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PWService implements IDao<PW> {

    @Autowired
    private PWRepository pwRepository;

    @Autowired
    private StudentPWService studentPWService;


    @Override
    public PW create(PW pw) {
        pwRepository.save(pw);
        return pw;
    }

    @Override
    public PW update(PW newpw) {
        return pwRepository.save(newpw);
    }

    @Override
    public boolean delete(PW pw) {
        try {
            pw.setTooth(null);

            List<StudentPW> listlignes = studentPWService.findAll();
            for(StudentPW l : listlignes){
                //List<Long> listidphar ;
                if(l.getPw().getId() == pw.getId()){
                    StudentPWKey id = new StudentPWKey(l.getStudent().getId(), pw.getId());
                    studentPWService.delete(id);
                    pwRepository.delete(pw);
                }

            }
            pwRepository.delete(pw);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<PW> findAll() {
        return pwRepository.findAll();
    }

    @Override
    public PW findById(Long id) {
        return pwRepository.findById(id).orElse(null);
    }
}
