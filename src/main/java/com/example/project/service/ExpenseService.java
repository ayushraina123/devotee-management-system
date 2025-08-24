package com.example.project.service;

import com.example.project.dtos.ExpenseDto;
import com.example.project.entities.Expense;
import com.example.project.enums.ExpenseType;
import com.example.project.exceptions.NegativeBalanceException;
import com.example.project.exceptions.NotFoundException;
import com.example.project.repositories.ExpenseRepository;
import com.example.project.specs.SpecificationsQueryBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final DonationService donationService;

    public ExpenseService(ExpenseRepository expenseRepository, DonationService donationService) {
        this.donationService = donationService;
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseDto> getAllExpenseDtos(int pageNumber, int pageSize, String payeeName, BigDecimal amount,
                                              ExpenseType expenseType) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name", "amount").ascending());
        Integer expenseTypeId = null != expenseType ? expenseType.getId() : null;
        Specification<Expense> specification = SpecificationsQueryBuilder.specificationsForExpense(payeeName, amount, expenseTypeId);
        Page<Expense> expenses = expenseRepository.findAll(specification, pageable);
        return ExpenseDto.convertToDto(expenses.stream().toList()).stream().toList();
    }

    public void saveExpense(List<ExpenseDto> expenseDtos) {
        BigDecimal totalExpense = expenseDtos.stream().map(ExpenseDto::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (getAvailableBalance().subtract(totalExpense).compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeBalanceException(
                    "Balance is getting below 0",
                    List.of(String.format("Available balance: %s, attempted expense: %s", getAvailableBalance(), totalExpense))
            );
        }
        List<Expense> expensesToSave = ExpenseDto.convertToEntity(expenseDtos);
        expenseRepository.saveAllAndFlush(expensesToSave);
    }

    public void deleteExpense(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new NotFoundException("No expense found " +
                "with the associated id : " + expenseId));
        expenseRepository.delete(expense);
    }

    public BigDecimal getAvailableBalance() {
        return donationService.getDonationSum().subtract(getExpenseSum());
    }

    public BigDecimal getExpenseSum() {
        return expenseRepository.getTotalExpenseAmount();
    }
}
