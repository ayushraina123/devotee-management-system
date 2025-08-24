package com.example.project.batch.tasklets;

import com.example.project.repositories.DevoteeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteDevoteeTasklet implements Tasklet {

    private final DevoteeRepository devoteeRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            devoteeRepository.deleteAll();
            System.out.println("All devotees deleted successfully.");
        } catch (Exception e) {
            System.err.println("Failed to delete devotees: " + e.getMessage());
            throw e;
        }
        return RepeatStatus.FINISHED;
    }
}
