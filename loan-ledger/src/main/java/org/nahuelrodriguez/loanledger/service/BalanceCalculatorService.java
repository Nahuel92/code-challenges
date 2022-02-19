package org.nahuelrodriguez.loanledger.service;

import org.nahuelrodriguez.loanledger.entity.Advance;
import org.nahuelrodriguez.loanledger.entity.Balance;
import org.nahuelrodriguez.loanledger.entity.SavedLoanEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
public class BalanceCalculatorService {
    private static final BigDecimal FIXED_RATE = BigDecimal.valueOf(0.00035);
    private BigDecimal aggregateAdvanceBalance = BigDecimal.ZERO;
    private BigDecimal interestPayableBalance = BigDecimal.ZERO;
    private BigDecimal totalInterestPaid = BigDecimal.ZERO;

    public Balance calculate(final Stream<SavedLoanEvent> savedLoanEvents, final String date) {
        aggregateAdvanceBalance = interestPayableBalance = totalInterestPaid = BigDecimal.ZERO;
        LocalDate lastEventDate = null;
        final var advances = new LinkedHashMap<Long, Advance>();

        final var id = new AtomicLong(1);
        for (final var loanEvent : savedLoanEvents.toList()) {
            updateFees(lastEventDate, loanEvent.date());
            lastEventDate = loanEvent.date();

            if (loanEvent.type().equals("advance")) {
                advances.put(id.get(), advanceFrom(id.getAndIncrement(), loanEvent));
                aggregateAdvanceBalance = aggregateAdvanceBalance.add(loanEvent.amount());
                continue;
            }
            aggregateAdvanceBalance = aggregateAdvanceBalance.subtract(loanEvent.amount());
            processPayment(loanEvent.amount(), advances);
        }
        updateFees(lastEventDate, LocalDate.parse(date).plusDays(1));
        return new Balance(aggregateAdvanceBalance, interestPayableBalance, totalInterestPaid, Set.copyOf(advances.values()));
    }

    private Advance advanceFrom(final Long id, final SavedLoanEvent loanEvent) {
        if (aggregateAdvanceBalance.compareTo(BigDecimal.ZERO) >= 0) {
            return new Advance(id, loanEvent.date(), loanEvent.amount(), loanEvent.amount());
        }
        if (aggregateAdvanceBalance.add(loanEvent.amount()).compareTo(BigDecimal.ZERO) <= 0) {
            return new Advance(id, loanEvent.date(), loanEvent.amount(), BigDecimal.ZERO);
        }
        return new Advance(id, loanEvent.date(), loanEvent.amount(), aggregateAdvanceBalance.add(loanEvent.amount()));
    }

    private void updateFees(final LocalDate lastEventDate, final LocalDate date) {
        if (lastEventDate == null) {
            return;
        }
        interestPayableBalance = lastEventDate.datesUntil(date)
                .map(e -> getDailyAccruedInterest())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(interestPayableBalance);
    }

    private BigDecimal getDailyAccruedInterest() {
        return aggregateAdvanceBalance.compareTo(BigDecimal.ZERO) > 0 ?
                aggregateAdvanceBalance.multiply(FIXED_RATE) :
                BigDecimal.ZERO;
    }

    private void processPayment(BigDecimal payAmount, final Map<Long, Advance> advances) {
        payAmount = payInterestPayableBalance(payAmount);
        if (payAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        for (final var entry : advances.entrySet()) {
            if (entry.getValue().balance().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            if (payAmount.subtract(entry.getValue().balance()).compareTo(BigDecimal.ZERO) >= 0) {
                totalInterestPaid = totalInterestPaid.add(interestPayableBalance);
                payAmount = payAmount.subtract(entry.getValue().balance());
                advances.put(entry.getKey(), new Advance(entry.getValue().id(), entry.getValue().date(), entry.getValue().originalAmount(), BigDecimal.ZERO));
                continue;
            }
            totalInterestPaid = totalInterestPaid.add(interestPayableBalance);
            advances.put(entry.getKey(), new Advance(entry.getValue().id(), entry.getValue().date(), entry.getValue().originalAmount(), entry.getValue().balance().subtract(payAmount)));
            payAmount = BigDecimal.ZERO;
        }
    }

    private BigDecimal payInterestPayableBalance(final BigDecimal payAmount) {
        if (payAmount.subtract(interestPayableBalance).compareTo(BigDecimal.ZERO) < 0) {
            totalInterestPaid = totalInterestPaid.add(payAmount);
            interestPayableBalance = interestPayableBalance.subtract(payAmount);
            return BigDecimal.ZERO;
        }
        aggregateAdvanceBalance = aggregateAdvanceBalance.add(interestPayableBalance);
        totalInterestPaid = totalInterestPaid.add(interestPayableBalance);
        final var remaining = payAmount.subtract(interestPayableBalance);
        interestPayableBalance = BigDecimal.ZERO;
        return remaining;
    }
}
