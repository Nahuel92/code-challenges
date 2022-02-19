package generator

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import static org.assertj.core.api.Assertions.assertThat

@Subject(NewFullNames)
@Title("Unit tests for the NewFullNames class")
class NewFullNamesTest extends Specification {
    def "Success on generating a modified names list"() {
        given: "an arbitrary value for new names"
        def n = 3
        and: "test data"
        def inputData = ["Smith"    : ["Carl", "John"],
                         "Ortiz"    : ["Carl", "Robert", "Peter"],
                         "Rodriguez": ["Nahuel", "Robert"],
                         "McFly"    : ["Marty", "Ben", "Peter"],
                         "Parker"   : ["Peter"]
        ]

        when: "generating the modified names list"
        def results = NewFullNames.generate(inputData, n)

        then: "the modified names list has to contain the specified amount of names"
        results.size() == n
        and: "it should contain new names"
        assertThat(results).doesNotContainAnyElementsOf(
                [
                        Map.entry("Smith", "Carl"),
                        Map.entry("Ortiz", "Robert"),
                        Map.entry("Rodriguez", "Nahuel")
                ]
        )
    }
}
