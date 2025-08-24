package com.example.project.controller.batch.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/job")
@RequiredArgsConstructor
public class BatchJobController {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @PostMapping(value = "/delete-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> triggerJob(@RequestParam String jobName) {
        try {
            Job job = jobRegistry.getJob(jobName);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis()) // Uniqueness
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);
            return ResponseEntity.ok("Job '" + jobName + "' triggered successfully.");
        } catch (NoSuchJobException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No job found with name: " + jobName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Job execution failed: " + e.getMessage());
        }
    }

    @PostMapping("/export-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> triggerExportJob(@RequestParam String jobName, @RequestParam String donationType) {
        try {
            Job job = jobRegistry.getJob(jobName);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .addString("donationType", donationType)
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            if (jobExecution.getStatus() == BatchStatus.FAILED) {
                Throwable exception = jobExecution.getAllFailureExceptions().isEmpty() ? null : jobExecution.getAllFailureExceptions().get(0);

                String errorMessage = (exception != null) ? exception.getMessage() : "Job failed due to unknown error.";

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Job failed: " + errorMessage);
            }

            return ResponseEntity.ok(
                    "Job '" + jobName + "' triggered successfully for donationType: " + donationType
                            + ". Check your Downloads folder for the Excel file."
            );
        } catch (NoSuchJobException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No job found with name: " + jobName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Job execution failed: " + e.getMessage());
        }
    }
}
