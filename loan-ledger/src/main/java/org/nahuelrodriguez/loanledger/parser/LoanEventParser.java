package org.nahuelrodriguez.loanledger.parser;

import org.nahuelrodriguez.loanledger.entity.LoanEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class LoanEventParser {
    public LoanEvent parse(final String row) {
        if (!row.matches(".*,.*,.*")) {
            throw new IllegalArgumentException("Each row must contain 3 columns");
        }
        final String[] columns = row.split(",");
        final String type = columns[0];
        final var date = LocalDate.parse(columns[1]);
        final var amount = new BigDecimal(columns[2]);
        return new LoanEvent(type, date, amount);
    }
}
