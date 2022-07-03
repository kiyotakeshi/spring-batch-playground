package com.example.component;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SampleComponent {

    public int random(){
        return new Random().nextInt(100);
    }
}
