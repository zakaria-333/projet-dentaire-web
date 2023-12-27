package com.projet.depouilledentaire.dao;

import java.util.List;

public interface IDao <T>{
    T create(T O);
    T update(T O);
    boolean delete(T o);
    List<T> findAll();
    T findById(Long id);
}