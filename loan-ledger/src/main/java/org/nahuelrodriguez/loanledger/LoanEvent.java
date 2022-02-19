package org.nahuelrodriguez.loanledger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record LoanEvent(String type, LocalDate date, BigDecimal amount) {
}

record SavedLoanEvent(Long id, String type, LocalDate date, BigDecimal amount) {
}

class Advance {
    private final LocalDate date;
    private final BigDecimal originalAmount;
    private BigDecimal balance;

    public Advance(final LocalDate date, final BigDecimal originalAmount) {
        this.date = date;
        this.originalAmount = originalAmount;
        this.balance = originalAmount;
    }

    public Advance(final LocalDate date, final BigDecimal originalAmount, final BigDecimal balance) {
        this.date = date;
        this.originalAmount = originalAmount;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advance advance = (Advance) o;
        return Objects.equals(date, advance.date) && Objects.equals(originalAmount, advance.originalAmount) && Objects.equals(balance, advance.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, originalAmount, balance);
    }
}
