package org.nahuelrodriguez.loanledger.service;

import org.nahuelrodriguez.loanledger.entity.Advance;
import org.nahuelrodriguez.loanledger.entity.Balance;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Collection;

@Service
public class BalancePrinterService {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public void print(final Balance balance) {
        printHeader();
        printBalances(balance.advances());
        printSummaryStatistics(balance);
    }

    private void printHeader() {
        System.out.println("Advances:");
        System.out.println("----------------------------------------------------------");
        System.out.format("%10s%11s%17s%20s%n", "Identifier", "Date", "Initial Amt", "Current Balance");
    }

    private void printBalances(final Collection<Advance> advances) {
        advances.forEach(e -> System.out.format("%10s%11s%17s%20s%n",
                        e.id(),
                        e.date(),
                        DECIMAL_FORMAT.format(e.originalAmount()),
                        DECIMAL_FORMAT.format(e.balance())
                )
        );
    }

    private void printSummaryStatistics(final Balance balance) {
        System.out.println("\nSummary Statistics:");
        System.out.println("----------------------------------------------------------");
        System.out.format("Aggregate Advance Balance:%31s%n", DECIMAL_FORMAT.format(balance.aggregateAdvanceBalance()));
        System.out.format("Interest Payable Balance:%32s%n", DECIMAL_FORMAT.format(balance.summaryStatistics().getInterestPayableBalance()));
        System.out.format("Total Interest Paid:%37s%n", DECIMAL_FORMAT.format(balance.summaryStatistics().getTotalInterestPaid()));
        System.out.format("Balance Applicable to Future Advances:%19s%n", DECIMAL_FORMAT.format(balance.balanceApplicableToFutureAdvances()));
    }
}
