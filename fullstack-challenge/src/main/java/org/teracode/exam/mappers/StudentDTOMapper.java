package org.teracode.exam.mappers;

import org.teracode.exam.entities.Student;
import org.teracode.exam.responses.dtos.StudentDTO;

import java.util.stream.Collectors;

public class StudentDTOMapper {
    private final SubjectDTOMapper subjectDTOMapper;

    public StudentDTOMapper() {
        this.subjectDTOMapper = new SubjectDTOMapper();
    }

    public StudentDTO from(final Student student) {
        final var subjectDTOS = student.getSubjectsEnrolled()
                .stream()
                .map(subjectDTOMapper::from)
                .collect(Collectors.toSet());

        final var dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setBirth(student.getBirth().toString());
        dto.setSubjectsEnrolled(subjectDTOS);

        return dto;
    }
}
