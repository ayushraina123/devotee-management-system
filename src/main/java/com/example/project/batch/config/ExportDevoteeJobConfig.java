package com.example.project.batch.config;

import com.example.project.batch.steps.DevoteeExcelWriter;
import com.example.project.batch.steps.DevoteeProcessor;
import com.example.project.batch.steps.DevoteeReader;
import com.example.project.dtos.DevoteeExcelRowDto;
import com.example.project.entities.Devotee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ExportDevoteeJobConfig {

    @Bean
    public Step exportDevoteesStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   DevoteeReader reader,
                                   DevoteeProcessor processor,
                                   DevoteeExcelWriter writer) {
        return new StepBuilder("exportDevoteesStep", jobRepository)
                .<Devotee, DevoteeExcelRowDto>chunk(100, transactionManager)
                .reader(reader)       // Reads devotees filtered by donationType
                .processor(processor) // Converts to Excel row format
                .writer(writer)       // Writes to Excel using Apache POI
                .faultTolerant()
                .noSkip(RuntimeException.class)
                .build();
    }

    @Bean
    public Job exportDevoteesJob(JobRepository jobRepository,
                                 Step exportDevoteesStep) {
        return new JobBuilder("exportDevoteesJob", jobRepository)
                .start(exportDevoteesStep)
                .build();
    }
}
