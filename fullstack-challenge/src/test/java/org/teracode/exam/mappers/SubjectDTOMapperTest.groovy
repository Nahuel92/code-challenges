package org.teracode.exam.mappers

import org.teracode.exam.entities.Subject
import spock.lang.Specification

class SubjectDTOMapperTest extends Specification {
    private SubjectDTOMapper mapper

    void setup() {
        mapper = new SubjectDTOMapper()
    }

    def "Given a valid subject when invocking from then -> valid dto created"() {
        given:
        def subject = new Subject()
        subject.setId(1L)
        subject.setDescription("Test subject")

        when:
        def result = mapper.from(subject)

        then:
        noExceptionThrown()
        result.getId() == subject.getId()
        result.getDescription() == subject.getDescription()
    }
}
