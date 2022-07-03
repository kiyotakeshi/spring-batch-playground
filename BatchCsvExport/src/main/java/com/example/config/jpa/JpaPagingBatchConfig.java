package com.example.config.jpa;

import com.example.config.BaseConfig;
import com.example.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

@Configuration
public class JpaPagingBatchConfig extends BaseConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    @StepScope
    public JpaPagingItemReader<Employee> jpaPagingReader() {
        String sql = "select * from employee where gender = :genderParam";

        JpaNativeQueryProvider<Employee> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(sql);
        queryProvider.setEntityClass(Employee.class);

        HashMap<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("genderParam", 1);

        return new JpaPagingItemReaderBuilder<Employee>()
                .entityManagerFactory(entityManagerFactory)
                .name("jpaPagingItemReader")
                .queryProvider(queryProvider)
                .parameterValues(parameterValues)
                .pageSize(5)
                .build();
    }

    @Bean
    public Step exportJpaPagingStep() {
        return this.stepBuilderFactory.get("ExportJpaPagingStep")
                .<Employee, Employee>chunk(10)
                .reader(jpaPagingReader()).listener(readListener)
                .processor(this.genderConvertProcessor)
                .writer(csvWriter()).listener(writeListener)
                .build();
    }

    @Bean("JpaPagingJob")
    public Job exportJpaPagingJob() {
        return this.jobBuilderFactory.get("ExportJpaPagingJob")
                .incrementer(new RunIdIncrementer())
                .start(exportJpaPagingStep())
                .build();
    }
}
