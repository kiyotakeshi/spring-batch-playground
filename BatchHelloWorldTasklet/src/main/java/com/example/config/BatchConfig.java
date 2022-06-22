package com.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Qualifier("HelloTasklet")
    private final Tasklet helloTasklet;

    @Qualifier("HelloTasklet2")
    private final Tasklet helloTasklet2;

    @Bean
    public JobParametersValidator defaultValidator() {
        // パラメータの必須チェックができる
        var validator = new DefaultJobParametersValidator();

        String[] requiredKeys = {"run.id", "require"};
        validator.setRequiredKeys(requiredKeys);

        String[] optionKeys = {"option"};
        validator.setOptionalKeys(optionKeys);

        validator.afterPropertiesSet();

        return validator;
    }

    @Bean
    public Step taskletStep1() {
        return stepBuilderFactory
                .get("HelloTaskletStep1")// step名としてDBに登録される
                .tasklet(helloTasklet)
                .build();
    }

    @Bean
    public Step taskletStep2() {
        return stepBuilderFactory
                .get("HelloTaskletStep2")// step名としてDBに登録される
                .tasklet(helloTasklet2)
                .build();
    }

    @Bean
    public Job taskletJob() {
        return jobBuilderFactory.get("HelloWorldTaskletJob")
                .incrementer(new RunIdIncrementer())
                .start(taskletStep1())
                .next(taskletStep2())
                .validator(defaultValidator())
                .build();
    }
}
