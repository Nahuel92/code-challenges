package org.teracode.exam.daos.implementations;

import org.springframework.stereotype.Repository;
import org.teracode.exam.daos.StudentsDao;
import org.teracode.exam.entities.Student;
import org.teracode.exam.entities.Subject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class StudentsMockDao implements StudentsDao {
    @Override
    public Collection<Student> getStudents() {
        return Set.of(
                createStudent("Peter", "Robinson"), createStudent("David", "Hudson"),
                createStudent("Mike", "Thompson"), createStudent("Ross", "Jackson"),
                createStudent("Joey", "Trump"), createStudent("Diego", "Williams"),
                createStudent("Patrick", "Roberts"), createStudent("Larry", "Jane"),
                createStudent("John", "Whitman"), createStudent("Carl", "Johnson"),
                createStudent("Hugh", "Blackman")
        );
    }

    private Student createStudent(final String firstName, final String lastName) {
        final var student = new Student();
        student.setId(ThreadLocalRandom.current().nextLong());
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setBirth(generateRandomDate());
        addSubjects(student);
        return student;
    }

    private void addSubjects(final Student student) {
        final var subjects = Set.of(createSubject(1L, "Maths"),
                createSubject(2L, "History"), createSubject(3L, "Chemistry"),
                createSubject(4L, "Science"), createSubject(5L, "Music"));

        final var amountOfSubjects = ThreadLocalRandom.current().nextLong(0, subjects.size());

        subjects.stream().limit(amountOfSubjects).forEach(student::addSubjectEnrolled);
    }

    private LocalDate generateRandomDate() {
        final var initialDate = LocalDate.of(1950, 1, 1);
        final var finalDate = LocalDate.of(2010, 12, 31);

        final long days = initialDate.until(finalDate, ChronoUnit.DAYS);
        final long randomDays = ThreadLocalRandom.current().nextLong(days + 1);

        return initialDate.plusDays(randomDays);
    }

    private Subject createSubject(final Long id, final String description) {
        final var subject = new Subject();
        subject.setId(id);
        subject.setDescription(description);
        return subject;
    }
}
