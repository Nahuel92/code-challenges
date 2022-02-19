package org.nahuelrodriguez.loanledger.repository


import org.nahuelrodriguez.loanledger.entity.LoanEvent
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate

import static org.assertj.core.api.Assertions.assertThat

@Subject(LoanEventRepository)
@Title("Unit test for the LoanEventRepository class")
class LoanEventRepositoryTest extends Specification {
    private LoanEventRepository subject

    void setup() {
        final def dataSource = DataSourceBuilder.create()
                .type(SimpleDriverDataSource.class)
                .driverClassName("org.sqlite.JDBC")
                .url("jdbc:sqlite:db.tmp")
                .build()

        final def jdbcTemplate = new JdbcTemplate(dataSource)
        subject = new LoanEventRepository(jdbcTemplate, "db.tmp")
    }

    def "Success on creating database"() {
        given: "a file path"
        def filePath = Path.of("db.tmp")

        when: "creating a database"
        subject.createDB()

        then: "the file must exist"
        Files.exists(filePath)

        cleanup: "resources created by this test"
        Files.delete(filePath)
    }

    def "Success on deleting existing database"() {
        given: "a temporal file containing the database"
        File.createTempFile("db.tmp", null)

        when: "dropping the database"
        subject.dropDB()

        then: "the database file must no exist"
        !Files.exists(Path.of("db.tmp"))
    }

    def "Success on inserting/fetching data into/from existing database"() {
        given: "a collection of loans"
        def loans = [new LoanEvent("advance", LocalDate.now(), BigDecimal.ONE)]
        and: "a database created"
        subject.createDB()

        when: "inserting the loans into the database"
        subject.insert(loans)

        then: "the database file must exist"
        Files.exists(Path.of("db.tmp"))
        and: "it must contain the inserted loan"
        def results = subject.fetch(LocalDate.now().toString())
        assertThat(results.toList())
                .first()
                .hasFieldOrPropertyWithValue("type", loans.get(0).type())
                .hasFieldOrPropertyWithValue("date", loans.get(0).date())
                .hasFieldOrPropertyWithValue("amount", loans.get(0).amount())

        cleanup: "resources created by this test"
        Files.delete(Path.of("db.tmp"))
    }
}
