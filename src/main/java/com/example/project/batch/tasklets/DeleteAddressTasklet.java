package com.example.project.batch.tasklets;

import com.example.project.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAddressTasklet implements Tasklet {

    private final AddressRepository addressRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            addressRepository.deleteAll();
            System.out.println("All addresses deleted successfully.");
        } catch (Exception e) {
            System.err.println("Failed to delete address: " + e.getMessage());
            throw e;
        }
        return RepeatStatus.FINISHED;
    }
}
