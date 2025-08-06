package com.example.workhorse.config.batch;

import com.example.workhorse.batch.MarketDeviceItemWriter;
import com.example.workhorse.batch.MarketItemReader;
import com.example.workhorse.data.entity.Market;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class MarketDeviceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MarketItemReader marketItemReader;
    private final MarketDeviceItemWriter marketDeviceItemWriter;

    public MarketDeviceJobConfig(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 MarketItemReader marketItemReader,
                                 MarketDeviceItemWriter marketDeviceItemWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.marketItemReader = marketItemReader;
        this.marketDeviceItemWriter = marketDeviceItemWriter;
    }

    @Bean
    public Job marketDeviceJob() {
        return new JobBuilder("marketDeviceJob", jobRepository)
                .start(marketDeviceStep())
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