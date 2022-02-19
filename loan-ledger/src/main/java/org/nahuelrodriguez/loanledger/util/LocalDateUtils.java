package org.nahuelrodriguez.loanledger.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static boolean isValidDate(final String endDate) {
        try {
            LocalDate.parse(endDate, FORMATTER);
            return true;
        } catch (final IllegalArgumentException | DateTimeParseException e) {
            return false;
        }
    }
}
