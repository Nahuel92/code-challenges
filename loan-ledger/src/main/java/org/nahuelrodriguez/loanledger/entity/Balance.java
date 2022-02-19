package org.nahuelrodriguez.loanledger.entity;

import java.math.BigDecimal;
import java.util.Set;
import java.util.function.Predicate;

public record Balance(BigDecimal aggregateAdvanceBalance, BigDecimal interestPayableBalance,
                      BigDecimal totalInterestPaid, Set<Advance> advances) {

    public BigDecimal aggregateAdvanceBalance() {
        return getAbsoluteValueOrZero(e -> e.compareTo(BigDecimal.ZERO) >= 0);
    }

    public BigDecimal balanceApplicableToFutureAdvances() {
        return getAbsoluteValueOrZero(e -> e.compareTo(BigDecimal.ZERO) < 0);
    }

    private BigDecimal getAbsoluteValueOrZero(final Predicate<BigDecimal> condition) {
        return condition.test(aggregateAdvanceBalance) ?
                aggregateAdvanceBalance.abs() : BigDecimal.ZERO;
    }
}
