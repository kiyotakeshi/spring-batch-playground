package com.example.processor;

import com.example.domain.Employee;
import com.example.repository.EmployeeJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("ExistsCheckProcessor")
@StepScope
@Slf4j
@RequiredArgsConstructor
public class ExistsCheckProcessor implements ItemProcessor<Employee, Employee> {

    private final EmployeeJdbcRepository employeeJdbcRepository;

    @Override
    public Employee process(Employee item) throws Exception {
        boolean exists = employeeJdbcRepository.exists(item.getId());

        if(exists){
            log.info("SKIP!!.Already exists employee: {}", item);
            // writer に渡さない
            return null;
        }

        return item;
    }
}
