package com.example.chunk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class HelloReader implements ItemReader<String> {

    // reader が null を返すとそのstepは終了する
    private String[] input = {"hello", "world", "spring", "batch", null, "chunk"};
    private int index = 0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String message = input[index++];
        log.info("read:{}", message);
        return message;
    }
}
