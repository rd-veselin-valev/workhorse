package com.example.workhorse.config.batch;

import com.example.workhorse.batch.MarketDeviceItemWriter;
import com.example.workhorse.batch.MarketItemReader;
import com.example.workhorse.data.entity.Market;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class MarketDeviceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MarketItemReader marketItemReader;
    private final MarketDeviceItemWriter marketDeviceItemWriter;
    private final JobExecutionListener jobExecutionListener;

    @Bean
    public Job marketDeviceJob() {
        return new JobBuilder("marketDeviceJob", jobRepository)
                .start(marketDeviceStep())
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    public Step marketDeviceStep() {
        return new StepBuilder("marketDeviceStep", jobRepository)
                .<Market, Market>chunk(1000, transactionManager)
                .reader(marketItemReader)
                .writer(marketDeviceItemWriter)
                .build();
    }
}