package com.projet.depouilledentaire.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Professor extends User{

    private String grade;

    @OneToMany(mappedBy = "professor")
    private List<Groupe> groups;

    @OneToMany(mappedBy = "professor")
    private List<PW> pws;

}
