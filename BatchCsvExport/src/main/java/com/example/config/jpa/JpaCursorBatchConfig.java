package com.example.config.jpa;

import com.example.config.BaseConfig;
import com.example.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

@Configuration
public class JpaCursorBatchConfig extends BaseConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    @StepScope
    public JpaCursorItemReader<Employee> jpaCursorReader() {
        String sql = "select * from employee where gender = :genderParam";

        JpaNativeQueryProvider<Employee> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(sql);
        queryProvider.setEntityClass(Employee.class);

        HashMap<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("genderParam", 1);

        return new JpaCursorItemReaderBuilder<Employee>()
                .entityManagerFactory(entityManagerFactory)
                .name("jpaCursorItemReader")
                .queryProvider(queryProvider)
                .parameterValues(parameterValues)
                .build();
    }

    @Bean
    public Step exportJpaCursorStep() {
        return this.stepBuilderFactory.get("ExportJpaCursorStep")
                .<Employee, Employee>chunk(10)
                .reader(jpaCursorReader()).listener(readListener)
                .processor(this.genderConvertProcessor)
                .writer(csvWriter()).listener(writeListener)
                .build();
    }

    @Bean("JpaCursorJob")
    public Job exportJpaCursorJob() {
        return this.jobBuilderFactory.get("ExportJpaCursorJob")
                .incrementer(new RunIdIncrementer())
                .start(exportJpaCursorStep())
                .build();
    }
}
