package com.example.domain;

import lombok.Data;

@Data
public class Employee {
    private Integer id;
    private String name;
    private Integer age;
    private Integer gender;
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
