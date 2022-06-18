package com.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Tasklet tasklet;

    @Bean
    public Step taskletStep1() {
        return stepBuilderFactory
                .get("HelloTaskletStep1")// step名としてDBに登録される
                .tasklet(tasklet)
                .build();
    }

    @Bean
    public Job taskletJob() {
        return jobBuilderFactory.get("HelloWorldTaskletJob")
                .incrementer(new RunIdIncrementer())
                .start(taskletStep1())
                .build();
    }
}
