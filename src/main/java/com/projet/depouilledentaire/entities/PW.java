package com.projet.depouilledentaire.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Entity
@Getter
@Setter
@Data
public class PW {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String title ;
    private String objectif ;
    
    @Lob
    @Column(name = "doc", columnDefinition = "LONGBLOB")
    private byte[] doc;

    @ManyToMany(mappedBy = "pws")
    private List<Groupe> groups;

    @OneToMany(mappedBy = "pw")
    private List<StudentPW> studentPWList;

    @ManyToOne
    private Tooth tooth;

    @ManyToOne
    private Professor professor;

}
