package com.example.config.parallel;

import com.example.config.BaseConfig;
import com.example.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ParallelBatchConfig extends BaseConfig {

    @Autowired
    private JdbcPagingItemReader<Employee> jdbcPagingReader;

    @Bean
    public TaskExecutor asyncTaskExecutor(){
        return new SimpleAsyncTaskExecutor("parallel_");
    }

    @Bean
    public Step exportParallelStep() {
        return stepBuilderFactory.get("ExportParallellStep")
                .<Employee, Employee>chunk(10)
                .reader(jdbcPagingReader).listener(readListener)
                .processor(this.genderConvertProcessor)
                .writer(csvWriter()).listener(writeListener)
                .taskExecutor(asyncTaskExecutor())
                .throttleLimit(3)
                .build();
    }

    @Bean
    public Job exportParallelJob() {
        return jobBuilderFactory.get("ExportParallelJob")
                .incrementer(new RunIdIncrementer())
                .start(exportParallelStep())
                .build();
    }
}
