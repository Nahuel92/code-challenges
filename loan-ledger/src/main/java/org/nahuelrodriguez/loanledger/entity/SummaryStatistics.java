package org.nahuelrodriguez.loanledger.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SummaryStatistics {
    private static final BigDecimal FIXED_RATE = BigDecimal.valueOf(0.00035);
    private BigDecimal aggregateAdvanceBalance = BigDecimal.ZERO;
    private BigDecimal interestPayableBalance = BigDecimal.ZERO;
    private BigDecimal totalInterestPaid = BigDecimal.ZERO;
    private LocalDate lastEventDate;

    public BigDecimal getAggregateAdvanceBalance() {
        return aggregateAdvanceBalance;
    }

    public BigDecimal addAggregateAdvanceBalance(final BigDecimal amount) {
        return aggregateAdvanceBalance = aggregateAdvanceBalance.add(amount);
    }

    public void subtractAggregateAdvanceBalance(final BigDecimal amount) {
        aggregateAdvanceBalance = aggregateAdvanceBalance.subtract(amount);
    }

    public BigDecimal getInterestPayableBalance() {
        return interestPayableBalance;
    }

    public void addInterestPayableBalance(final BigDecimal amount) {
        interestPayableBalance = interestPayableBalance.add(amount);
    }

    public void subtractInterestPayableBalance(final BigDecimal amount) {
        interestPayableBalance = interestPayableBalance.subtract(amount);
    }

    public void resetInterestPayableBalance() {
        this.interestPayableBalance = BigDecimal.ZERO;
    }

    public BigDecimal getTotalInterestPaid() {
        return totalInterestPaid;
    }

    public void addTotalInterestPaid(final BigDecimal additionalInterestPaid) {
        totalInterestPaid = totalInterestPaid.add(additionalInterestPaid);
    }

    public boolean isAggregateAdvanceBalancePositive() {
        return aggregateAdvanceBalance.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isUpdatedAggregateAdvanceBalanceNegative(final BigDecimal amount) {
        return aggregateAdvanceBalance.add(amount).compareTo(BigDecimal.ZERO) <= 0;
    }

    public BigDecimal getDailyAccruedInterest() {
        return aggregateAdvanceBalance.compareTo(BigDecimal.ZERO) > 0 ?
                aggregateAdvanceBalance.multiply(FIXED_RATE) :
                BigDecimal.ZERO;
    }

    public void addInterestPayableBalanceToTotalInterestPaid() {
        addTotalInterestPaid(interestPayableBalance);
    }

    public void addInterestPayableBalanceToAggregateAdvanceBalance() {
        addAggregateAdvanceBalance(interestPayableBalance);
    }

    public void updateFees(final LocalDate date) {
        if (lastEventDate == null) {
            lastEventDate = date;
            return;
        }
        addInterestPayableBalance(
                lastEventDate.datesUntil(date)
                        .map(e -> getDailyAccruedInterest())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        lastEventDate = date;
    }

    public Advance advanceFrom(final Long id, final SavedLoanEvent loanEvent) {
        if (isAggregateAdvanceBalancePositive()) {
            return new Advance(id, loanEvent.date(), loanEvent.amount(), loanEvent.amount());
        }
        if (isUpdatedAggregateAdvanceBalanceNegative(loanEvent.amount())) {
            return new Advance(id, loanEvent.date(), loanEvent.amount(), BigDecimal.ZERO);
        }
        return new Advance(id, loanEvent.date(), loanEvent.amount(), addAggregateAdvanceBalance(loanEvent.amount()));
    }
}
