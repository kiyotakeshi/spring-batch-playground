package com.example.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Employee {
    @Id
    private Integer id;
    private String name;
    private Integer age;
    private Integer gender;
    @Transient
    private String genderString;

    public void convertGenderIntToString() {
        if (gender == 1) {
            genderString = "man";
        } else if (gender == 2) {
            genderString = "female";
        } else {
            String errorMsg = "Gender is invalid:" + gender;
            throw new IllegalStateException(errorMsg);
        }
    }
}

