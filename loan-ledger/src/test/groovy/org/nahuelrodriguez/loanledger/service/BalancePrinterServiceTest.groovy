package org.nahuelrodriguez.loanledger.service

import org.nahuelrodriguez.loanledger.entity.Advance
import org.nahuelrodriguez.loanledger.entity.Balance
import spock.lang.Specification

import java.time.LocalDate

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut

class BalancePrinterServiceTest extends Specification {
    private BalancePrinterService subject

    def setup() {
        subject = new BalancePrinterService()
    }

    def "Success on printing the balance"() {
        given: "a set of advances"
        def advances = Set.of(new Advance(1L, LocalDate.now(), new BigDecimal(150.50), new BigDecimal(165.25)))
        and: "a balance"
        def balance = new Balance(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.TEN, advances)

        when: "printing the balance"
        def output = tapSystemOut(() -> subject.print(balance))

        then: "the balance report should contain the expected text and format"
        output == getExpectedOutput(balance)
    }

    private String getExpectedOutput(final Balance balance) {
        return "Advances:\n" +
                "----------------------------------------------------------\n" +
                String.format("%10s%11s%17s%20s%n", "Identifier", "Date", "Initial Amt", "Current Balance") +
                balance.advances().stream()
                        .map(e ->
                                String.format("%10s%11s%17s%20s%n",
                                        e.id(),
                                        e.date(),
                                        e.originalAmount().round(2),
                                        e.balance().round(2)
                                )
                        ).toList()
                        .join("") +
                "\nSummary Statistics:\n" +
                "----------------------------------------------------------\n" +
                String.format("Aggregate Advance Balance:%31s%n", balance.aggregateAdvanceBalance().round(2)) +
                String.format("Interest Payable Balance:%32s%n", balance.interestPayableBalance().round(2)) +
                String.format("Total Interest Paid:%37s%n", balance.totalInterestPaid().round(2)) +
                String.format("Balance Applicable to Future Advances:%19s%n", balance.balanceApplicableToFutureAdvances().round(2))
    }
}
