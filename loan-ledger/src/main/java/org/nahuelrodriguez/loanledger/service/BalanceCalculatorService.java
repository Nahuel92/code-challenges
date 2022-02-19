package org.nahuelrodriguez.loanledger.service;

import org.nahuelrodriguez.loanledger.entity.Advance;
import org.nahuelrodriguez.loanledger.entity.Balance;
import org.nahuelrodriguez.loanledger.entity.SavedLoanEvent;
import org.nahuelrodriguez.loanledger.entity.SummaryStatistics;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
public class BalanceCalculatorService {
    public Balance calculate(final Stream<SavedLoanEvent> savedLoanEvents, final String date) {
        final var advances = new LinkedHashMap<Long, Advance>();
        final var summaryStatistics = new SummaryStatistics();
        final var id = new AtomicLong(1);

        for (final var loanEvent : savedLoanEvents.toList()) {
            summaryStatistics.updateFees(loanEvent.date());
            if (loanEvent.type().equals("advance")) {
                advances.put(id.get(), summaryStatistics.advanceFrom(id.getAndIncrement(), loanEvent));
                summaryStatistics.addAggregateAdvanceBalance(loanEvent.amount());
                continue;
            }
            summaryStatistics.subtractAggregateAdvanceBalance(loanEvent.amount());
            processPayment(loanEvent.amount(), advances, summaryStatistics);
        }
        summaryStatistics.updateFees(LocalDate.parse(date).plusDays(1));
        return new Balance(summaryStatistics, advances.values());
    }

    private void processPayment(BigDecimal payAmount, final Map<Long, Advance> advances, final SummaryStatistics summaryStatistics) {
        payAmount = payInterestPayableBalance(payAmount, summaryStatistics);
        if (payAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        for (final var entry : advances.entrySet()) {
            if (entry.getValue().balance().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            if (payAmount.subtract(entry.getValue().balance()).compareTo(BigDecimal.ZERO) >= 0) {
                summaryStatistics.addInterestPayableBalanceToTotalInterestPaid();
                payAmount = payAmount.subtract(entry.getValue().balance());
                advances.put(entry.getKey(), new Advance(entry.getValue().id(), entry.getValue().date(), entry.getValue().originalAmount(), BigDecimal.ZERO));
                continue;
            }
            summaryStatistics.addInterestPayableBalanceToTotalInterestPaid();
            advances.put(entry.getKey(), new Advance(entry.getValue().id(), entry.getValue().date(), entry.getValue().originalAmount(), entry.getValue().balance().subtract(payAmount)));
            payAmount = BigDecimal.ZERO;
        }
    }

    private BigDecimal payInterestPayableBalance(final BigDecimal payAmount, final SummaryStatistics summaryStatistics) {
        if (payAmount.subtract(summaryStatistics.getInterestPayableBalance()).compareTo(BigDecimal.ZERO) < 0) {
            summaryStatistics.addTotalInterestPaid(payAmount);
            summaryStatistics.subtractInterestPayableBalance(payAmount);
            return BigDecimal.ZERO;
        }
        summaryStatistics.addInterestPayableBalanceToAggregateAdvanceBalance();
        summaryStatistics.addInterestPayableBalanceToTotalInterestPaid();
        final var remaining = payAmount.subtract(summaryStatistics.getInterestPayableBalance());
        summaryStatistics.resetInterestPayableBalance();
        return remaining;
    }
}
