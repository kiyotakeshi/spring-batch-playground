package com.example.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Employee {
    @NotNull
    @Id
    private Integer id;

    @NotNull
    private String name;

    @Min(20)
    private Integer age;
    private Integer gender;

    // insert,update しないフィールド
    @Transient
    private String genderString;

    public void convertGenderStringToInt(){
        if("man".equals(genderString)){
            gender = 1;
        } else if("female".equals(genderString)){
            gender = 2;
        } else {
            throw new IllegalArgumentException("gender string is invalid: " + genderString);
        }
    }
}
