package com.example.domain;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Employee {
    @NotNull
    private Integer id;

    @NotNull
    private String name;

    @Min(20)
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
