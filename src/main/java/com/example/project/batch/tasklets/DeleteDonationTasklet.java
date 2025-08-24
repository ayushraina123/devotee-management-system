package com.example.project.batch.tasklets;

import com.example.project.repositories.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteDonationTasklet implements Tasklet {

    private final DonationRepository donationRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            donationRepository.deleteAll();
            System.out.println("All donations deleted successfully.");
        } catch (Exception e) {
            System.err.println("Failed to delete donations: " + e.getMessage());
            throw e;
        }
        return RepeatStatus.FINISHED;
    }
}
