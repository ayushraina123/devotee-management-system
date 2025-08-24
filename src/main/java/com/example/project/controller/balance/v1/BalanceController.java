package com.example.project.controller.balance.v1;

import com.example.project.exceptions.NegativeBalanceException;
import com.example.project.service.DonationService;
import com.example.project.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final ExpenseService expenseService;
    private final DonationService donationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> fetchBalance() {
        BigDecimal balance = donationService.getDonationSum().subtract(expenseService.getExpenseSum());

        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeBalanceException("Available Bank balance is less than 0");
        }
        return ResponseEntity.ok(balance);
    }
}
