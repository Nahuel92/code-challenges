package calculator

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import static org.assertj.core.api.Assertions.*

@Subject(CommonFirstNames)
@Title("Unit tests for the CommonFirstNames class")
class CommonFirstNamesTest extends Specification {
    def "Success on calculating the top 10 first names"() {
        given: "test data"
        def inputData = ["Smith"    : ["Carl", "John"],
                         "Ortiz"    : ["Carl", "Robert", "Peter"],
                         "Rodriguez": ["Nahuel", "Robert"],
                         "McFly"    : ["Marty", "Ben", "Peter"],
                         "Parker"   : ["Peter"]
        ]

        when: "calculating the common first names"
        def results = CommonFirstNames.calculate(inputData).toList()

        then: "the results have to contain up to 10 last names"
        results.size() <= 10
        and: "should be properly ordered"
        assertThat(results).containsExactlyElementsOf(
                [
                        Map.entry("Peter", 3L),
                        Map.entry("Robert", 2L),
                        Map.entry("Carl", 2L),
                        Map.entry("Nahuel", 1L),
                        Map.entry("John", 1L),
                        Map.entry("Marty", 1L),
                        Map.entry("Ben", 1L)
                ]
        )
    }
}
