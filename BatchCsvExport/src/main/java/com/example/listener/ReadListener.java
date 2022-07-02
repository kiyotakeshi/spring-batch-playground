package com.example.listener;

import com.example.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReadListener implements ItemReadListener<Employee> {

    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(Employee item) {
        log.debug("AfterRead: {}", item);
    }

    @Override
    public void onReadError(Exception ex) {
        log.error("ReadError: errorMessage={}", ex.getMessage(), ex);
    }
}
