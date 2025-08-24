package com.example.project.batch.tasklets;

import com.example.project.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteExpenseTasklet implements Tasklet {

    private final ExpenseRepository expenseRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            expenseRepository.deleteAll();
            System.out.println("All expenses deleted successfully.");
        } catch (Exception e) {
            System.err.println("Failed to delete expenses: " + e.getMessage());
            throw e;
        }
        return RepeatStatus.FINISHED;
    }
}
