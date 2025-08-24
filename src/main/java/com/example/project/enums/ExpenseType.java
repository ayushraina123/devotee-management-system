package com.example.project.enums;

import lombok.Getter;

@Getter
public enum ExpenseType {
    CONSTRUCTION(1),
    MAINTENANCE(2),
    ELECTRICITY(3),
    WATER(4),
    FUNCTION(5),
    DONATION_OUTFLOW(6),
    PURCHASES(7),
    SALARIES(8),
    DECORATION(9),
    SECURITY(10),
    CLEANING(11),
    RENOVATION(12),
    MARKETING(13),
    LEGAL_FEES(14),
    PROPERTY_TAX(15),
    MISCELLANEOUS(16);

    private int id;

    ExpenseType(int id) {
        this.id = id;
    }

    public static ExpenseType getExpenseTypeFromId(int id) {
        for (ExpenseType expenseType : ExpenseType.values()) {
            if (expenseType.id == id) {
                return expenseType;
            }
        }
        return null;
    }
}
