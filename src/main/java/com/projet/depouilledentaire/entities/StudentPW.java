package com.projet.depouilledentaire.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
public class StudentPW {

    @EmbeddedId
    private StudentPWKey id;

    @ManyToOne
    @MapsId("student_id")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("pw_id")
    @JoinColumn(name = "pw_id")
    private PW pw;

    private String time;

    @Lob
    @Column(name = "imageFront", columnDefinition = "LONGBLOB")
    private byte[] imageFront;

    @Lob
    @Column(name = "imageSide", columnDefinition = "LONGBLOB")
    private byte[] imageSide;

    private double af1;
    private double af2;
    private double bf1;
    private double bf2;
    private double cf1;
    private double cf2;


    private double as1;
    private double as2;
    private double bs1;
    private double bs2;
    private double cs1;
    private double cs2;

    private Date date;

    private double convergence;
    private String isSymetrical;
    private double note;
    public String remarque;

    public StudentPW(Student student, PW pw, String time, byte[] imageFront, byte[] imageSide, Date date) {
        StudentPWKey id = new StudentPWKey(student.getId(), pw.getId());
        this.id = id;
        this.time = time;
        this.imageFront = imageFront;
        this.imageSide = imageSide;
        this.date = date;
    }



}
