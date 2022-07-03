package com.example.config.jdbc;

import com.example.config.BaseConfig;
import com.example.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class JdbcPagingBatchConfig extends BaseConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlPagingQueryProviderFactoryBean queryProvider(){
        var provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(dataSource);
        provider.setSelectClause("select id, name, age, gender");
        provider.setFromClause("from employee");
        provider.setWhereClause("where gender= :genderParam");
        provider.setSortKey("id");

        return provider;
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<Employee> jdbcPagingReader() throws Exception {
        HashMap<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("genderParam", 1);

        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);

        // PagingItemReader はSQLを分割して実行する=並列処理ができるため
        // CursorItemReader はデータの件数が少ない、処理の順番を守らないといけないものに有効
        return new JdbcPagingItemReaderBuilder<Employee>()
                .name("jdbcPagingItemReader")
                .dataSource(dataSource)
                .queryProvider(queryProvider().getObject())
                .parameterValues(parameterValues)
                .rowMapper(rowMapper)
                // 一度に select で取得する件数
                .pageSize(5)
                .build();
    }

    @Bean
    public Step exportJdbcPagingStep() throws Exception {
        return this.stepBuilderFactory.get("ExportJdbcPagingStep")
                .<Employee, Employee>chunk(10)
                .reader(jdbcPagingReader()).listener(readListener)
                .processor(this.genderConvertProcessor)
                .writer(csvWriter()).listener(writeListener)
                .build();
    }

    @Bean("JdbcPagingJob")
    public Job exportJdbcPagingJob() throws Exception {
        return this.jobBuilderFactory.get("ExportJdbcPagingJob")
                .incrementer(new RunIdIncrementer())
                .start(exportJdbcPagingStep())
                .build();
    }
}
