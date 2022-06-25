package com.example.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:sample.properties")
@Getter
@ToString
public class SampleProperty {

    @Value("${csv.path}")
    private String csvPath;
}
