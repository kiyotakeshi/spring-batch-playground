package com.example.chunk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class HelloReader implements ItemReader<String> {

    // reader が null を返すとそのstepは終了する
    private String[] input = {"hello", "world", "spring", "batch", null, "chunk"};
    private int index = 0;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution){
        ExecutionContext jobContext = stepExecution.getJobExecution()
                .getExecutionContext();
        jobContext.put("jobKey", "jobValue from HelloReader");

        ExecutionContext stepContext = stepExecution.getExecutionContext();
        stepContext.put("stepKey", "stepValue from HelloReader");
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String message = input[index++];
        log.info("read:{}", message);
        return message;
    }
}
