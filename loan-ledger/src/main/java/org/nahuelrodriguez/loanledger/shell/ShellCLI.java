package org.nahuelrodriguez.loanledger.shell;

import org.nahuelrodriguez.loanledger.entity.LoanEvent;
import org.nahuelrodriguez.loanledger.parser.LoanEventParser;
import org.nahuelrodriguez.loanledger.repository.LoanEventRepository;
import org.nahuelrodriguez.loanledger.service.BalanceCalculatorService;
import org.nahuelrodriguez.loanledger.service.BalancePrinterService;
import org.nahuelrodriguez.loanledger.util.LocalDateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class ShellCLI {
    private final LoanEventRepository loanEventRepository;
    private final BalanceCalculatorService balanceCalculatorService;
    private final BalancePrinterService balancePrinterService;
    private final LoanEventParser loanEventParser;
    private final String dbName;

    ShellCLI(final LoanEventRepository loanEventRepository, final BalanceCalculatorService balanceCalculatorService,
             final BalancePrinterService balancePrinterService, final LoanEventParser loanEventParser,
             @Value("${db.name}") final String dbName) {
        this.loanEventRepository = loanEventRepository;
        this.balanceCalculatorService = balanceCalculatorService;
        this.balancePrinterService = balancePrinterService;
        this.loanEventParser = loanEventParser;
        this.dbName = dbName;
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
            System.out.println("Database does not exist at " + dbName + ", please create it using `create-db` command");
            return;
        }

        final List<LoanEvent> set;
        try (final var content = Files.lines(path)) {
            set = content.map(loanEventParser::parse).collect(Collectors.toList());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        final var count = loanEventRepository.insert(set);
        System.out.println("Loaded " + count + " events from " + path.toAbsolutePath());
    }

    @ShellMethod("Gets loan balances")
    void balances(@ShellOption(defaultValue = "") String endDate) {
        if (endDate.isBlank()) {
            endDate = LocalDate.now().toString();
        }

        if (!LocalDateUtils.isValidDate(endDate)) {
            System.out.println("Invalid date. Enter balance valid date in balance valid format(yyyy-MM-dd) ");
            return;
        }
        final var savedLoanEvents = loanEventRepository.fetch(endDate);
        final var balance = balanceCalculatorService.calculate(savedLoanEvents, endDate);
        balancePrinterService.print(balance);
    }
}
