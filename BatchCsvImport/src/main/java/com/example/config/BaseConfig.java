package com.example.config;

import com.example.domain.Employee;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@EnableBatchProcessing
public abstract class BaseConfig {

    @Autowired
    protected JobBuilderFactory jobBuilderFactory;

    @Autowired
    protected StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("GenderConvertProcessor")
    protected ItemProcessor<Employee, Employee> genderConvertProcessor;

    @Autowired
    @Qualifier("ExistsCheckProcessor")
    protected ItemProcessor<Employee, Employee> existsCheckProcessor;

    @Autowired
    protected BeanValidatingItemProcessor<Employee> beanValidatingItemProcessor;

    @Autowired
    protected ItemReadListener<Employee> readListener;

    @Autowired
    protected ItemProcessListener<Employee, Employee> processListener;

    @Autowired
    protected ItemWriteListener<Employee> writeListener;

    @Autowired
    protected SampleProperty property;

    @Bean
    @StepScope
    public FlatFileItemReader<Employee> csvReader() {
        String[] csvColumnNames = {"id", "name", "age", "genderString"};

        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeCsvReader")
                // ?????????????????????????????????????????? FileSystemResource ??????????????????
                .resource(new ClassPathResource(property.getCsvPath()))
                .linesToSkip(1)
                .encoding(StandardCharsets.UTF_8.name())
                .delimited()
                // tsv ?????????????????????????????????
                // .delimiter(DelimitedLineTokenizer.DELIMITER_TAB)
                .names(csvColumnNames)
                // ???????????????Java????????????????????????????????????????????????????????????????????????
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Employee.class);
                }})
                .build();

        // new JsonFileItemWriterBuilder<>()
    }

    @Bean
    @StepScope
    public ItemProcessor<Employee, Employee> compositeProcessor(){
        CompositeItemProcessor<Employee, Employee> compositeItemProcessor = new CompositeItemProcessor<>();

        compositeItemProcessor.setDelegates(Arrays.asList(
                this.beanValidatingItemProcessor,
                this.existsCheckProcessor,
                this.genderConvertProcessor
        ));

        return compositeItemProcessor;
    }

    @Bean
    @StepScope
    public BeanValidatingItemProcessor<Employee> validationProcessor() {
        BeanValidatingItemProcessor<Employee> validatingItemProcessor = new BeanValidatingItemProcessor<>();

        // true: ????????????????????????????????????????????????????????????????????????????????????
        // false: ValidationException ???????????????????????????????????????
        validatingItemProcessor.setFilter(true);

        return validatingItemProcessor;
    }
}
