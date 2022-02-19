package org.nahuelrodriguez.loanledger;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@Service
public class BalanceCalculator {
    BigDecimal aggregateAdvanceBalance = BigDecimal.ZERO;
    BigDecimal interestPayableBalance = BigDecimal.ZERO;
    BigDecimal totalInterestPaid = BigDecimal.ZERO;

    public Balance calculate(final List<SavedLoanEvent> savedLoanEvents, final String date) {
        LocalDate lastEventDate = null;
        final var advances = new LinkedHashSet<Advance>();
        for (var loanEvent : savedLoanEvents) {
            updateFees(lastEventDate, loanEvent.date());
            lastEventDate = loanEvent.date();

            if (loanEvent.type().equals("advance")) {
                final Advance advance;
                if (aggregateAdvanceBalance.compareTo(BigDecimal.ZERO) < 0) {
                    if (aggregateAdvanceBalance.add(loanEvent.amount()).compareTo(BigDecimal.ZERO) <= 0) {
                        advance = new Advance(loanEvent.date(), loanEvent.amount(), BigDecimal.ZERO);
                    } else {
                        advance = new Advance(loanEvent.date(), loanEvent.amount(), aggregateAdvanceBalance.add(loanEvent.amount()));
                    }
                } else {
                    advance = new Advance(loanEvent.date(), loanEvent.amount());
                }
                advances.add(advance);
                aggregateAdvanceBalance = aggregateAdvanceBalance.add(loanEvent.amount());
                continue;
            }
            aggregateAdvanceBalance = aggregateAdvanceBalance.subtract(loanEvent.amount());
            processPayment(loanEvent.amount(), advances);
        }

        updateFees(lastEventDate, LocalDate.parse(date).plusDays(1));
        final var output = new Balance(aggregateAdvanceBalance, interestPayableBalance, totalInterestPaid, advances);
        aggregateAdvanceBalance = interestPayableBalance = totalInterestPaid = BigDecimal.ZERO;
        return output;
    }

    private void updateFees(final LocalDate lastEventDate, final LocalDate date) {
        if (lastEventDate == null) {
            return;
        }

        final BigDecimal dailyAccruedInterest;
        if (aggregateAdvanceBalance.compareTo(BigDecimal.ZERO) > 0) {
            dailyAccruedInterest = aggregateAdvanceBalance.multiply(BigDecimal.valueOf(0.00035));
        } else {
            dailyAccruedInterest = BigDecimal.ZERO;
        }

        final var feesUpToDate = lastEventDate.datesUntil(date)
                .map(e -> dailyAccruedInterest)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        interestPayableBalance = interestPayableBalance.add(feesUpToDate);
        // aggregateAdvanceBalance = aggregateAdvanceBalance.add(feesUpToDate);
    }

    private void processPayment(BigDecimal payAmount, final Set<Advance> advances) {
        if (payAmount.subtract(interestPayableBalance).compareTo(BigDecimal.ZERO) >= 0) {
            aggregateAdvanceBalance = aggregateAdvanceBalance.add(interestPayableBalance);
            totalInterestPaid = totalInterestPaid.add(interestPayableBalance);
            payAmount = payAmount.subtract(interestPayableBalance);
            interestPayableBalance = BigDecimal.ZERO;
        } else {
            totalInterestPaid = totalInterestPaid.add(payAmount);
            interestPayableBalance = interestPayableBalance.subtract(payAmount);
            payAmount = BigDecimal.ZERO;
        }

        if (payAmount.compareTo(BigDecimal.ZERO) > 0) {
            for (var entry : advances) {
                if (entry.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                if (payAmount.subtract(entry.getBalance()).compareTo(BigDecimal.ZERO) >= 0) {
                    totalInterestPaid = totalInterestPaid.add(interestPayableBalance);
                    payAmount = payAmount.subtract(entry.getBalance());
                    entry.setBalance(BigDecimal.ZERO);
                    continue;
                }
                totalInterestPaid = totalInterestPaid.add(interestPayableBalance);
                entry.setBalance(entry.getBalance().subtract(payAmount));
                payAmount = BigDecimal.ZERO;
            }
        }
    }
}

record Balance(BigDecimal aggregateAdvanceBalance, BigDecimal interestPayableBalance,
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
