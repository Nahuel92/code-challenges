package file

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import static org.assertj.core.api.Assertions.assertThat

@Subject(Reader)
@Title("Unit tests for the Reader class")
class ReaderTest extends Specification {
    def "Success on reading a file"() {
        given: "test data"
        def fileName = 'test/resources/test-file.txt'

        when: "reading the file content"
        def results = Reader.read(fileName)

        then: "the results contain the proper amount of items"
        results.size() == 4
        and: "should be properly ordered"
        assertThat(results).containsExactlyEntriesOf(
                [
                        "Smith"  : ["Joan", "Sam"],
                        "Thomas" : ["Joan"],
                        "Upton"  : ["Joan"],
                        "Cartman": ["Eric"]
                ]
        )
    }
}
