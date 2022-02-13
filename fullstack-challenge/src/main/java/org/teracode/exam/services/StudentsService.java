package org.teracode.exam.services;

import org.teracode.exam.responses.dtos.StudentDTO;

import java.util.Collection;

public interface StudentsService {
    Collection<StudentDTO> retrieveStudentsGroupedByLastNameInitial(final String lastNameInitial);

    Collection<StudentDTO> retrieveStudentsTakingSubjects();

    Collection<StudentDTO> retrieveStudentsTakingSubjects(final Long subjectId);
}
