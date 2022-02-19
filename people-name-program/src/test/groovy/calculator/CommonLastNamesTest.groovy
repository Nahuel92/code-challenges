package calculator

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import static org.assertj.core.api.Assertions.assertThat

@Subject(CommonLastNames)
@Title("Unit tests for the CommonLastNames class")
class CommonLastNamesTest extends Specification {
    def "Success on calculating the top 10 last names"() {
        given: "test data"
        def inputData = ["Smith"    : ["Carl", "John"],
                         "Ortiz"    : ["Carl", "Robert", "Peter"],
                         "Rodriguez": ["Nahuel", "Robert"],
                         "McFly"    : ["Marty", "Ben", "Peter"],
                         "Parker"   : ["Peter"]
        ]

        when: "calculating the common last names"
        def results = CommonLastNames.calculate(inputData).toList()

        then: "the results have to contain up to 10 last names"
        results.size() <= 10
        and: "should be properly ordered"
        assertThat(results).containsExactlyElementsOf(
                [
                        Map.entry("Ortiz", ["Carl", "Robert", "Peter"]),
                        Map.entry("McFly", ["Marty", "Ben", "Peter"]),
                        Map.entry("Smith", ["Carl", "John"]),
                        Map.entry("Rodriguez", ["Nahuel", "Robert"]),
                        Map.entry("Parker", ["Peter"])
                ]
        )
    }
}
