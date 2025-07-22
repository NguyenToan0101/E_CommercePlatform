package org.example.ecommerce.common.dto.seller;

import java.time.LocalDate;

public class CashflowDTO {
    private LocalDate date;
    private long income;
    private long expense;

    public CashflowDTO() {}
    public CashflowDTO(LocalDate date, long income, long expense) {
        this.date = date;
        this.income = income;
        this.expense = expense;
    }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public long getIncome() { return income; }
    public void setIncome(long income) { this.income = income; }
    public long getExpense() { return expense; }
    public void setExpense(long expense) { this.expense = expense; }
} 