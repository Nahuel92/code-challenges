package org.nahuelrodriguez.loanledger;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class ShellComp {
    private final LoanEventRepository loanEventRepository;
    private final Balance balance;

    ShellComp(final LoanEventRepository loanEventRepository, final Balance balance) {
        this.loanEventRepository = loanEventRepository;
        this.balance = balance;
    }

    @ShellMethod("Initializes the SQLite database")
    void createDb() {
        loanEventRepository.createDB();
    }

    @ShellMethod("Deletes the SQLite database")
    public void dropDb() throws IOException {
        loanEventRepository.dropDB();
    }

    @ShellMethod("Loads a CSV file that contains advance and payment events")
    void load(@ShellOption final String filePath) {
        System.out.println("Load events with data from csv file.");
        final var path = Path.of(filePath);
        if (!Files.exists(path)) {
            System.out.println("Database does not exist at " + path.toAbsolutePath() + ", please create it using `create-db` command");
            return;
        }

        final List<LoanEvent> set;
        try (final var content = Files.lines(path)) {
            set = content.map(LoanEventParser::parse).collect(Collectors.toList());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        final var count = loanEventRepository.insert(set);
        System.out.println("Loaded " + count + " events from " + path.toAbsolutePath());
    }

    @ShellMethod("Get loan balances")
    void balances(@ShellOption(defaultValue = "") String endDate) {
        if (endDate.isBlank()) {
            endDate = LocalDate.now().toString();
        }

        if (!isValidDate(endDate)) {
            System.out.println("Invalid date. Enter a valid date in a valid format(yyyy-MM-dd) ");
            return;
        }

        System.out.println("Display balance statistics as of " + endDate + ".");
        System.out.println("Advances:");
        System.out.println("----------------------------------------------------------");
        System.out.format("%10s%11s%17s%20s%n", "Identifier", "Date", "Initial Amt", "Current Balance");

        final var savedLoanEvents = loanEventRepository.fetch(endDate);
        final var a = balance.calculateBalance(savedLoanEvents);

        final var decimalFormat = new DecimalFormat("0.00");
        System.out.println("\nSummary Statistics:");
        System.out.println("----------------------------------------------------------");
        System.out.format("Aggregate Advance Balance:%31s%n", a.get("aggregateAdvanceBalance").compareTo(BigDecimal.ZERO) >= 0 ? decimalFormat.format(a.get("aggregateAdvanceBalance").abs()) : decimalFormat.format(0.0) );
        System.out.format("Interest Payable Balance:%32s%n", decimalFormat.format(a.get("interestPayableBalance")) );
        System.out.format("Total Interest Paid:%37s%n", decimalFormat.format(a.get("totalInterestPaid")));
        System.out.format("Balance Applicable to Future Advances:%19s%n", a.get("aggregateAdvanceBalance").compareTo(BigDecimal.ZERO) < 0 ? decimalFormat.format(a.get("aggregateAdvanceBalance").abs()) : decimalFormat.format(0.0) );
    }

    private boolean isValidDate(final String endDate) {
        try {
            LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (final IllegalArgumentException | DateTimeParseException e) {
            return false;
        }
    }
}

class LoanEventParser {
    public static LoanEvent parse(final String row) {
        final String[] columns = row.split(",");
        if (columns.length != 3) {
            throw new IllegalArgumentException("Each row must contain 3 columns");
        }

        final String type = columns[0];
        final var date = LocalDate.parse(columns[1]);
        final var amount = new BigDecimal(columns[2]);

        return new LoanEvent(type, date, amount);
    }
}

record LoanEvent(String type, LocalDate date, BigDecimal amount) {
}

record SavedLoanEvent(Long id, String type, LocalDate date, BigDecimal amount) {
}

class Advance {
    final LocalDate date;
    final BigDecimal originalAmount;
    BigDecimal balance;

    public Advance(final LocalDate date, final BigDecimal originalAmount, final BigDecimal balance) {
        this.date = date;
        this.originalAmount = originalAmount;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
