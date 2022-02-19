package org.nahuelrodriguez.loanledger.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SavedLoanEvent(Long id, String type, LocalDate date, BigDecimal amount) {
}
