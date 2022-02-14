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
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ShellComponent
public class ShellComp {
    private final LoanEventRepository loanEventRepository;

    ShellComp(final LoanEventRepository loanEventRepository) {
        this.loanEventRepository = loanEventRepository;
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

        final var overallAdvanceBalance = 0.0;
        final var overallInterestPayableBalance = 0.0;
        final var overallInterestPaid = 0.0;
        final var overallPaymentsForFuture = 0.0;

        System.out.println("Advances:");
        System.out.println("----------------------------------------------------------");
        System.out.println("Identifier" + " ".repeat(11) + "Date" + " ".repeat(17) + "Initial Amt" + " ".repeat(20) + "Current Balance");
        // TODO: FIXME Print each advance row and relevant advance statistics
        // print summary statistics

        final var formatter = new DecimalFormat("##.00");

        loanEventRepository.fetch()
                        .forEach(e -> System.out.println(" ".repeat(10) + e.id() + " ".repeat(11) + e.date() +
                                " ".repeat(17) + formatter.format(e.amount())

                        ));

        final var formatter2 = new DecimalFormat("0.00");

        System.out.println("\nSummary Statistics:");
        System.out.println("----------------------------------------------------------");
        System.out.println("Aggregate Advance Balance:" + " ".repeat(31) + formatter2.format(overallAdvanceBalance));
        System.out.println("Interest Payable Balance:" + " ".repeat(32) + formatter2.format(overallInterestPayableBalance));
        System.out.println("Total Interest Paid:" + " ".repeat(37) + formatter2.format(overallInterestPaid));
        System.out.println("Balance Applicable to Future Advances:" + " ".repeat(19) + formatter2.format(overallPaymentsForFuture));
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
        final LocalDate date = LocalDate.parse(columns[1]);
        final BigDecimal amount = new BigDecimal(columns[2]);

        return new LoanEvent(type, date, amount);
    }
}

record LoanEvent(String type, LocalDate date, BigDecimal amount) {
}

record SavedLoanEvent(Long id, String type, LocalDate date, BigDecimal amount) {
}
