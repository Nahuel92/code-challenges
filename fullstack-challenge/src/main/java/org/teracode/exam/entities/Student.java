package org.teracode.exam.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Student extends Person {
    @OneToMany
    @JoinColumn
    private final Collection<Subject> subjectsEnrolled;

    public Student() {
        this.subjectsEnrolled = new HashSet<>();
    }

    public Collection<Subject> getSubjectsEnrolled() {
        return Collections.unmodifiableCollection(subjectsEnrolled);
    }

    public void addSubjectEnrolled(final Subject subject) {
        subjectsEnrolled.add(subject);
    }

    public boolean containsSubjectById(final Long id) {
        return !subjectsEnrolled.stream()
                .filter(subject -> subject.getId().equals(id))
                .collect(Collectors.toSet())
                .isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(subjectsEnrolled, student.subjectsEnrolled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectsEnrolled);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", subjectsEnrolled=" + subjectsEnrolled +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birth=" + birth +
                '}';
    }
}
