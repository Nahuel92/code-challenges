package org.nahuelrodriguez.loanledger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public class LoanEventRepository {
    private final JdbcTemplate jdbcTemplate;
    private final Path db;

    LoanEventRepository(final JdbcTemplate jdbcTemplate, @Value("${db.name}") final String dbName) {
        this.jdbcTemplate = jdbcTemplate;
        this.db = Path.of(dbName);
    }

    public void createDB() {
        System.out.println("Initialize sqlite3 database.");
        if (Files.exists(db)) {
            System.out.println("Database already exists");
            return;
        }

        final var sql = """
                CREATE TABLE events (
                    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                    type VARCHAR(32) NOT NULL,
                    amount DECIMAL NOT NULL,
                    date_created DATE NOT NULL
                    CHECK (type IN ('advance', 'payment'))
                );
                """;
        jdbcTemplate.execute(sql);
        System.out.println("Initialized database at " + db.toAbsolutePath());
    }

    public void dropDB() throws IOException {
        System.out.println("Delete sqlite3 database.");
        if (Files.exists(db)) {
            Files.delete(db);
            System.out.println("Deleted SQLite database at " + db.toAbsolutePath());
            return;
        }
        System.out.println("SQLite database does not exist at " + db.toAbsolutePath());
    }

    @Transactional
    public Long insert(final List<LoanEvent> events) {
        return Arrays.stream(jdbcTemplate
                        .batchUpdate("INSERT INTO events(type, amount, date_created) VALUES (?, ?, ?);",
                                new BatchPreparedStatementSetter() {
                                    @Override
                                    public void setValues(final PreparedStatement ps, int i) throws SQLException {
                                        ps.setString(1, events.get(i).type());
                                        ps.setBigDecimal(2, events.get(i).amount());
                                        ps.setString(3, events.get(i).date().toString());
                                    }

                                    @Override
                                    public int getBatchSize() {
                                        return events.size();
                                    }
                                }
                        ))
                .count();
    }

    public List<SavedLoanEvent> fetch() {
        return fetch(LocalDate.now().toString());
    }

    public List<SavedLoanEvent> fetch(final String date) {
        final var sql = """
                SELECT id, type, date_created, amount
                FROM events
                WHERE date_created <= ?
                ORDER BY date_created;
                """;
        return jdbcTemplate
                .query(sql,
                        (rs, rn) -> new SavedLoanEvent(
                                rs.getLong(1),
                                rs.getString(2),
                                LocalDate.parse(rs.getString(3)),
                                rs.getBigDecimal(4)
                        ),
                        date
                );
    }
}
