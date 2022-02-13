package org.teracode.exam.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teracode.exam.daos.StudentsDao;
import org.teracode.exam.mappers.StudentDTOMapper;
import org.teracode.exam.responses.dtos.StudentDTO;
import org.teracode.exam.services.StudentsService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentsBasicService implements StudentsService {
    private final StudentsDao dao;
    private final StudentDTOMapper dtoMapper;

    @Autowired
    public StudentsBasicService(final StudentsDao dao) {
        this.dao = dao;
        this.dtoMapper = new StudentDTOMapper();
    }

    @Override
    public Collection<StudentDTO> retrieveStudentsGroupedByLastNameInitial(final String lastNameInitial) {
        return dao.getStudents()
                .stream()
                .filter(s -> s.getLastName().substring(0, 1).equalsIgnoreCase(lastNameInitial))
                .map(dtoMapper::from)
                .collect(Collectors.toSet());
    }

    public Collection<StudentDTO> retrieveStudentsTakingSubjects() {
        return dao.getStudents()
                .stream()
                .filter(s -> s.getSubjectsEnrolled().size() > 0)
                .map(dtoMapper::from)
                .collect(Collectors.toSet());
    }

    public Collection<StudentDTO> retrieveStudentsTakingSubjects(final Long id) {
        return dao.getStudents()
                .stream()
                .filter(student -> student.containsSubjectById(id))
                .map(dtoMapper::from)
                .collect(Collectors.toSet());
    }
}
