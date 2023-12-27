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

    private Date date;
    private double note;
    private String angles;
    private double af1;
    private double af2;
    private double bf1;
    private double bf2;
    private double cf1;
    private double cf2;
    private double cvf2;

    public StudentPW(Student student, PW pw, String time, byte[] imageFront, byte[] imageSide, Date date) {
        StudentPWKey id = new StudentPWKey(student.getId(), pw.getId());
        this.id = id;
        this.time = time;
        this.imageFront = imageFront;
        this.imageSide = imageSide;
        this.date = date;
    }


    public StudentPW(Student student, PW pw, String time, Date date, byte[] imageFront, byte[] imageSide, double af1, double af2, double bf1, double bf2, double cf1, double cf2, double cvf2, double note) {
        StudentPWKey id = new StudentPWKey(student.getId(),pw.getId());
        this.id = id;
        this.pw = pw;
        this.time = time;
        this.date = date;
        this.imageFront = imageFront;
        this.imageSide = imageSide;
        this.af1 = af1;
        this.af2 = af2;
        this.bf1 = bf1;
        this.bf2 = bf2;
        this.cf1 = cf1;
        this.cf2 = cf2;
        this.cvf2 = cvf2;
        this.note = note;
    }

}
