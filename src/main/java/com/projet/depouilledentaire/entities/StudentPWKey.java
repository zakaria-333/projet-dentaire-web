package com.projet.depouilledentaire.entities;


import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;



@Embeddable
public class StudentPWKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long student_id;
    private Long pw_id;

    public StudentPWKey(Long student_id, Long pw_id) {
        this.student_id = student_id;
        this.pw_id = pw_id;
    }

    public StudentPWKey(){}

    public Long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }

    public Long getPw_id() {
        return pw_id;
    }

    public void setPw_id(Long pw_id) {
        this.pw_id = pw_id;
    }
}