package com.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ItemReader<String> reader;
    private final ItemProcessor<String, String> processor;
    private final ItemWriter writer;

    @Bean
    public Step chunkStep() {
        return stepBuilderFactory.get("HelloChunkStep")
                // chunkSize = コミット間隔(一度に処理する件数)
                // chunkSize 分 reader,processor を実行し、最後に一回 writer を実行
                // writer が1回のみ実行なのは、まとめて書き込みを行ったほうが性能が良くなるため
                // なお、コミット間隔を大きくするとメモリ上で多くのデータを保持しておくことになる
                // writer を実行したタイミングでトランザクションがコミットされる
                .<String, String>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job chunkJob() throws Exception {
        return jobBuilderFactory.get("HelloWorldChunkJob")
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .build();
    }
}
