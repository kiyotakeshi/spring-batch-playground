package com.example.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

import static org.assertj.core.api.Assertions.assertThat;

// SpringBoot は実行しない
@ExtendWith(MockitoExtension.class)
@Slf4j
class Tasklet1Test {

    @InjectMocks
    private Tasklet1 tasklet1;

    @BeforeAll
    static void beforeAll() {
        log.info("------- start tasklet1 unit test");
    }

    @AfterAll
    static void afterAll() {
        log.info("------- end tasklet1 unit test");
    }

    @Test
    void checkRepeatStatus() throws Exception {
        StepContribution contribution = getStepContribution();
        RepeatStatus repeatStatus = tasklet1.execute(contribution, getChunkContext());

        assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
    }

    private ChunkContext getChunkContext() {
        StepExecution execution = getStepExecution();
        StepContext stepContext = new StepContext(execution);
        return new ChunkContext(stepContext);
    }

    private StepContribution getStepContribution() {
        StepExecution execution = getStepExecution();
        return execution.createStepContribution();
    }

    private StepExecution getStepExecution() {
        StepExecution execution = new StepExecution("stepName", getJobExecution());
        execution.getExecutionContext().putString("stepKey", "stepValue");
        return execution;
    }

    private JobExecution getJobExecution() {
        JobParameters params = new JobParametersBuilder()
                .addString("param", "paramTest")
                .toJobParameters();

        String jobName = "UnitTestJob";
        long instanceId = 1L;
        long executionId = 1L;
        JobExecution execution = MetaDataInstanceFactory
                .createJobExecution(jobName, instanceId, executionId, params);
        execution.getExecutionContext().putString("jobKey", "jobValue");

        return execution;
    }
}