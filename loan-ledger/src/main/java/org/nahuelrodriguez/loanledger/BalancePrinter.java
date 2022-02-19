package org.nahuelrodriguez.loanledger;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Collection;

@Component
public class BalancePrinter {
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
        var i = 1;
        for (final var e : advances) {
            System.out.format("%10s%11s%17s%20s%n",
                    i++,
                    e.getDate(),
                    DECIMAL_FORMAT.format(e.getOriginalAmount()),
                    DECIMAL_FORMAT.format(e.getBalance())
            );
        }
    }

    private void printSummaryStatistics(final Balance balance) {
        System.out.println("\nSummary Statistics:");
        System.out.println("----------------------------------------------------------");
        System.out.format("Aggregate Advance Balance:%31s%n", DECIMAL_FORMAT.format(balance.aggregateAdvanceBalance()));
        System.out.format("Interest Payable Balance:%32s%n", DECIMAL_FORMAT.format(balance.interestPayableBalance()));
        System.out.format("Total Interest Paid:%37s%n", DECIMAL_FORMAT.format(balance.totalInterestPaid()));
        System.out.format("Balance Applicable to Future Advances:%19s%n", DECIMAL_FORMAT.format(balance.balanceApplicableToFutureAdvances()));
    }
}
