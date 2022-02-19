package org.nahuelrodriguez.loanledger

import org.nahuelrodriguez.loanledger.entity.SavedLoanEvent
import org.nahuelrodriguez.loanledger.service.BalanceCalculatorService
import org.nahuelrodriguez.loanledger.entity.Advance
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import java.time.LocalDate

import static org.assertj.core.api.Assertions.assertThat

@Subject(BalanceCalculatorService)
@Title("Unit tests for the Balance class")
class BalanceCalculatorServiceTest extends Specification {
    private BalanceCalculatorService subject

    def setup() {
        subject = new BalanceCalculatorService()
    }

    def "Success calculating balance"() {
        given: "a test data file"
        def input = new File("src/test/resources/tests/test1.csv").text
                .lines()
                .map(e -> e.split(","))
                .map(e -> new SavedLoanEvent(1, e[0], LocalDate.parse(e[1]), new BigDecimal(e[2])))

        when: "calculating balance"
        def results = subject.calculate(input, "2021-05-25")

        then: "the summary statistics are correct"
        results.aggregateAdvanceBalance().round(2) == 0.00
        results.interestPayableBalance().round(2) == 0.00
        results.totalInterestPaid().round(2) == 0.88
        results.balanceApplicableToFutureAdvances().round(2) == 99.12
        and: "the advances reflect a valid balance"
        assertThat(results.advances())
                .containsExactlyInAnyOrder(
                        new Advance(1L, LocalDate.parse("2021-05-22"),
                                BigDecimal.valueOf(1000.00).round(2),
                                BigDecimal.ZERO
                        )
                )

    }
}
