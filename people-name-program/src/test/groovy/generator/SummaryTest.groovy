package generator

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Subject(Summary)
@Title("Unit tests for the Summary class")
class SummaryTest extends Specification {
    def "Success on generation full names summary"() {
        given: "test data"
        def inputData = ["Smith"    : ["Carl", "John"],
                         "Ortiz"    : ["Carl", "Robert", "Peter"],
                         "Rodriguez": ["Nahuel", "Robert"],
                         "McFly"    : ["Marty", "Ben", "Peter"],
                         "Parker"   : ["Peter"]
        ]

        when: "generating the full names summary"
        def results = Summary.generate(inputData)

        then: "the summary has to contain the 3 expected results"
        results.size() == 3
        and: "the calculations are correct"
        results.get("fullNames") == 11
        results.get("lastNames") == 5
        results.get("firstNames") == 7
    }
}
