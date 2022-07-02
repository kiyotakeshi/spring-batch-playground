package com.example.config.jdbc;

import com.example.config.BaseConfig;
import com.example.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

@Configuration
public class JdbcCursorBatchConfig extends BaseConfig {

    @Autowired
    private DataSource dataSource;

    private static final String SELECT_EMPLOYEE_SQL = "select * from employee where gender = ?";

    @Bean
    @StepScope
    public JdbcCursorItemReader<Employee> jdbcCursorReader(){
        Object[] params = {1};

        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);

        // CursorItemReader は JDBC のカーソル(select 結果の行を保持)を使ってデータを読み取るインターフェース
        return new JdbcCursorItemReaderBuilder<Employee>()
                .dataSource(this.dataSource)
                .name("jdbcCursorItemReader")
                .sql(SELECT_EMPLOYEE_SQL)
                .queryArguments(params)
                .rowMapper(rowMapper)
                .build();
    }

    @Bean
    public Step exportJdbcCursorStep() throws Exception {
        return this.stepBuilderFactory.get("ExportJdbcCursorStep")
                .<Employee, Employee>chunk(10)
                .reader(jdbcCursorReader()).listener(readListener)
                .processor(this.genderConvertProcessor)
                .writer(csvWriter()).listener(writeListener)
                .build();
    }

    @Bean("JdbcCursorJob")
    public Job exportJdbcCursorJob() throws Exception {
        return this.jobBuilderFactory.get("ExportJdbcCursorJob")
                .incrementer(new RunIdIncrementer())
                .start(exportJdbcCursorStep())
                .build();
    }
}
