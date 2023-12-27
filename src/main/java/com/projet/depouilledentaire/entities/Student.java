package com.projet.depouilledentaire.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    private String number ;
    @ManyToOne
    private Groupe groupe;
    @OneToMany(mappedBy = "student")
    private List<StudentPW> studentPWList;
}
