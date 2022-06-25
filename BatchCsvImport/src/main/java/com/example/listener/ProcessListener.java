package com.example.listener;

import com.example.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcessListener implements ItemProcessListener<Employee, Employee> {
    @Override
    public void beforeProcess(Employee item) {
    }

    @Override
    public void afterProcess(Employee item, Employee result) {
    }

    @Override
    public void onProcessError(Employee item, Exception e) {
        log.error("processError: item={}, errorMessage={}", item, e.getMessage(), e);
    }
}
