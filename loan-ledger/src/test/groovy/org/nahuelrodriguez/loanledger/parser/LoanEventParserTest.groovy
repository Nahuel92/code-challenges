package org.nahuelrodriguez.loanledger.parser

import org.nahuelrodriguez.loanledger.entity.LoanEvent

import java.time.LocalDate

import static org.assertj.core.api.Assertions.*
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Subject(LoanEventParser)
@Title("Unit tests for the LoanEventParser class")
class LoanEventParserTest extends Specification {
    private LoanEventParser subject

    void setup() {
        subject = new LoanEventParser()
    }

    def "Failure on parsing invalid test data rows"() {
        when: "parsing a row"
        subject.parse(row)

        then: "an exception must be thrown"
        def e = thrown(IllegalArgumentException)
        e.getMessage() == "Each row must contain 3 columns"

        where: "some invalid test data rows can be"
        row << ["advance,2021-05-221000.00", "advance2021-05-22,1000.00", "advance2021-05-221000.00"]
    }

    def "Success on parsing valid test data rows"() {
        given: "valid test data rows from file"
        def input = new File("src/test/resources/tests/test1.csv").text

        when: "parsing those rows"
        def results = input.lines()
                .map(subject::parse)
                .toList()

        then: "no exception must be thrown"
        noExceptionThrown()
        and: "the results must contain each row in order properly parsed"
        assertThat(results)
                .containsExactly(
                        new LoanEvent("advance", LocalDate.parse("2021-05-22"), new BigDecimal("1000.00")),
                        new LoanEvent("payment", LocalDate.parse("2021-05-24"), new BigDecimal("500")),
                        new LoanEvent("payment", LocalDate.parse("2021-05-25"), new BigDecimal("600"))
                )
    }
}
