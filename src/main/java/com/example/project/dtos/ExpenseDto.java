package com.example.project.dtos;

import com.example.project.entities.Expense;
import com.example.project.enums.ExpenseType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ExpenseDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Amount cannot be null")
    @Min(value = 1, message = "Amount cannot be less than 1")
    private BigDecimal amount;
    @NotNull(message = "Expense type cannot null")
    private ExpenseType expenseType;

    public static List<ExpenseDto> convertToDto(List<Expense> expenseList) {
        return expenseList.stream().map(expense ->
                        ExpenseDto.builder()
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .expenseType(ExpenseType.getExpenseTypeFromId(expense.getExpenseType()))
                                .build())
                .toList();
    }

    public static List<Expense> convertToEntity(List<ExpenseDto> expenseDtos) {
        return expenseDtos.stream().map(expenseDto ->
                        Expense.builder()
                                .name(expenseDto.getName())
                                .amount(expenseDto.getAmount())
                                .expenseType(expenseDto.getExpenseType().getId())
                                .build())
                .toList();
    }
}
