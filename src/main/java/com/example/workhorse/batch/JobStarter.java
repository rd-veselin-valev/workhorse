package com.example.workhorse.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobStarter implements ApplicationRunner {
    private final JobRepository jobRepository;
    private final Job job;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobExecution execution = jobRepository.createJobExecution(
                job.getName(),
                new JobParametersBuilder()
                        .addLong("timestamp", System.currentTimeMillis())
                        .toJobParameters()
        );
        job.execute(execution);
    }
}


