package org.teracode.exam.mappers;

import org.teracode.exam.entities.Subject;
import org.teracode.exam.responses.dtos.SubjectDTO;

public class SubjectDTOMapper {
    public SubjectDTO from(final Subject subject) {
        final var dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setDescription(subject.getDescription());
        return dto;
    }
}
