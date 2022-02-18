package org.nahuelrodriguez.loanledger;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class Balance {
    // suma el balance de los advances, saldo a favor o lo que falta pagar, puede ser negativo
    BigDecimal aggregateAdvanceBalance = BigDecimal.ZERO;

    // lo que falta pagar de fees, interes diario >= 0
    BigDecimal interestPayableBalance = BigDecimal.ZERO;

    // lo que pago en total de fees >= 0
    BigDecimal totalInterestPaid = BigDecimal.ZERO;

    public Map<String, BigDecimal> calculateBalance(final List<SavedLoanEvent> savedLoanEvents) {
        LocalDate lastEventDate = null;

        final var advances = new LinkedHashSet<Advance>();
        for (var loanEvent : savedLoanEvents) {
            updateFees(lastEventDate, loanEvent.date());
            lastEventDate = loanEvent.date();

            if (loanEvent.type().equals("advance")) {
                advances.add(new Advance(loanEvent.date(), loanEvent.amount(), loanEvent.amount()));
                aggregateAdvanceBalance = aggregateAdvanceBalance.add(loanEvent.amount());
                continue;
            }

            aggregateAdvanceBalance = aggregateAdvanceBalance.subtract(loanEvent.amount());
            processPayment(loanEvent.amount(), advances);
        }

        final var decimalFormat = new DecimalFormat("00.00");
        var i = 1;
        for (var e : advances) {
            System.out.format("%10s%11s%17s%20s%n",
                    i++,
                    e.getDate(),
                    decimalFormat.format(e.getOriginalAmount()),
                    decimalFormat.format(e.getBalance())
            );
        }

        final var a = aggregateAdvanceBalance;
        final var b = interestPayableBalance;
        final var c = totalInterestPaid;
        aggregateAdvanceBalance = interestPayableBalance = totalInterestPaid = BigDecimal.ZERO;

        return Map.ofEntries(
                Map.entry("aggregateAdvanceBalance", a),
                Map.entry("interestPayableBalance", b),
                Map.entry("totalInterestPaid", c)
        );
    }

    private void updateFees(final LocalDate lastEventDate, final LocalDate date) {
        if (lastEventDate == null) {
            return;
        }

        final BigDecimal dailyAccruedInterest;
        if (aggregateAdvanceBalance.compareTo(BigDecimal.ZERO) >= 0) {
            dailyAccruedInterest = aggregateAdvanceBalance.multiply(BigDecimal.valueOf(0.00035));
        } else {
            dailyAccruedInterest = BigDecimal.ZERO;
        }

        final var feesUpToDate = lastEventDate.datesUntil(date)
                .map(e -> dailyAccruedInterest)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        interestPayableBalance = interestPayableBalance.add(feesUpToDate);
        aggregateAdvanceBalance = aggregateAdvanceBalance.add(feesUpToDate);
    }

    private void processPayment(BigDecimal payAmount, final Set<Advance> advances) {
        // 1. bajar el daily fee
        if (payAmount.subtract(interestPayableBalance).compareTo(BigDecimal.ZERO) >= 0) {
            totalInterestPaid = totalInterestPaid.add(interestPayableBalance);
            payAmount = payAmount.subtract(interestPayableBalance);
            interestPayableBalance = BigDecimal.ZERO;
        } else {
            totalInterestPaid = totalInterestPaid.add(payAmount);
            interestPayableBalance = interestPayableBalance.subtract(payAmount);
            payAmount = BigDecimal.ZERO;
        }

        // 2. bajar el oldest advance
        if (payAmount.compareTo(BigDecimal.ZERO) > 0) {
            for (var entry : advances) {
                if (entry.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                    if (payAmount.subtract(entry.getBalance()).compareTo(BigDecimal.ZERO) >= 0) {
                        totalInterestPaid = totalInterestPaid.add(interestPayableBalance);
                        payAmount = payAmount.subtract(entry.getBalance());
                        entry.setBalance(BigDecimal.ZERO);
                    } else {
                        totalInterestPaid = totalInterestPaid.add(interestPayableBalance);
                        entry.setBalance(entry.getBalance().subtract(payAmount));
                        payAmount = BigDecimal.ZERO;
                    }
                }
            }
        }

        // 3. acumular saldo a favor
        if (payAmount.compareTo(BigDecimal.ZERO) > 0) {
            aggregateAdvanceBalance = aggregateAdvanceBalance.subtract(payAmount);
        }
    }
}
