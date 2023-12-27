package com.projet.depouilledentaire.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String code ;
    private String year  ;

    @ManyToOne
    private Professor professor;

    @OneToMany(mappedBy = "groupe")
    private List<Student> students;

    @ManyToMany
    @JoinTable(name = "pw_group",joinColumns = @JoinColumn(name = "group_id"),inverseJoinColumns = @JoinColumn(name = "pw_id"))
    private List<PW> pws;
}
