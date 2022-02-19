package org.nahuelrodriguez.loanledger.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Advance(Long id, LocalDate date, BigDecimal originalAmount, BigDecimal balance) {
}
