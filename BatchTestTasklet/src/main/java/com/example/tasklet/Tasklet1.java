package com.example.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class Tasklet1 implements Tasklet {

    @Value("#{jobParameters['param']}")
    private String param;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Tasklet1");
        log.info("param={}", param);
        ExecutionContext jobContext = contribution.getStepExecution()
                .getJobExecution()
                .getExecutionContext();
        log.info("jobKey={}", jobContext.get("jobKey"));

        ExecutionContext stepContext = contribution.getStepExecution().getExecutionContext();
        log.info("stepKey={}", stepContext.get("stepKey"));

        return RepeatStatus.FINISHED;
    }
}
