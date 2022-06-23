package com.example.tasklet;

import com.example.property.SampleProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("HelloTasklet")
@StepScope // step が異なると別のインスタンスが生成される(singleton ではない)
@Slf4j
@RequiredArgsConstructor
public class HelloTasklet implements Tasklet {

    // @StepScope,JobScope の bean は jobParameters を利用できる
    // $ java -jar BatchHelloWorldTasklet/target/BatchHelloWorldTasklet-0.0.1-SNAPSHOT.jar require="hoge" option=100
    @Value("#{jobParameters['require']}")
    private String require;

    @Value("#{jobParameters['option']}")
    private Integer option;

    private final SampleProperty sampleProperty;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("hello world");

        // ExecutionContext は実行結果を持つクラス
        // stepから次のstepに受け渡し可能
        ExecutionContext jobContext = stepContribution.getStepExecution()
                .getJobExecution()
                .getExecutionContext();

        jobContext.put("jobKey", "jobValue from HelloTasklet");

        // 実行中のstep内で使用可能、stepが変わると参照できない
        ExecutionContext stepContext = stepContribution.getStepExecution()
                .getExecutionContext();

        stepContext.put("stepKey", "stepValue from HelloTasklet");

        log.info("require={}",require);
        log.info("option={}",option);
        log.info("sample.property={}", sampleProperty.getSampleProperty());

        return RepeatStatus.FINISHED;
    }
}
