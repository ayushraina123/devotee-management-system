package com.example.project.batch.config;

import com.example.project.batch.tasklets.DeleteAddressTasklet;
import com.example.project.batch.tasklets.DeleteDevoteeTasklet;
import com.example.project.batch.tasklets.DeleteDonationTasklet;
import com.example.project.batch.tasklets.DeleteExpenseTasklet;
import org.springframework.batch.core.Job;
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
public class DeleteJobConfig {

    @Bean
    public Step deleteExpenseStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  DeleteExpenseTasklet tasklet) {
        return new StepBuilder("deleteExpenseStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Step deleteDonationStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   DeleteDonationTasklet tasklet) {
        return new StepBuilder("deleteDonationStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Step deleteDevoteeStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  DeleteDevoteeTasklet tasklet) {
        return new StepBuilder("deleteDevoteeStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Step deleteAddressStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  DeleteAddressTasklet tasklet) {
        return new StepBuilder("deleteAddressStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Job deleteDataJob(JobRepository jobRepository,
                             Step deleteExpenseStep,
                             Step deleteDonationStep,
                             Step deleteDevoteeStep,
                             Step deleteAddressStep) {

        return new JobBuilder("deleteDataJob", jobRepository)
                .start(deleteExpenseStep)
                .next(deleteDonationStep)
                .next(deleteDevoteeStep)
                .next(deleteAddressStep)
                .build();
    }
}
