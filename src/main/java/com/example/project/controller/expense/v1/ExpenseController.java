package com.example.project.controller.expense.v1;

import com.example.project.dtos.ExpenseDto;
import com.example.project.enums.ExpenseType;
import com.example.project.service.ExpenseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public List<ExpenseDto> getExpenseDtos(@RequestParam int pageNumber, @RequestParam int pageSize,
                                           @RequestParam(required = false) String payeeName,
                                           @RequestParam(required = false) BigDecimal amount,
                                           @RequestParam(required = false) ExpenseType expenseType) {
        return expenseService.getAllExpenseDtos(pageNumber, pageSize, payeeName, amount, expenseType);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public void saveExpenses(@RequestBody @Valid @NotEmpty(message = "Request body cannot be empty") List<ExpenseDto> expenseDtos) {
        expenseService.saveExpense(expenseDtos);
    }

    @DeleteMapping("/{expenseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteExpense(@PathVariable("expenseId") Long expenseId) {
        expenseService.deleteExpense(expenseId);
    }
}
