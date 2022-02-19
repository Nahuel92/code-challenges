package org.nahuelrodriguez.loanledger.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanEvent(String type, LocalDate date, BigDecimal amount) {
}
