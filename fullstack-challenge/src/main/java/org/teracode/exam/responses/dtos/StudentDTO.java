package org.teracode.exam.responses.dtos;

import java.util.Collection;
import java.util.Objects;

public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String birth;
    private Collection<SubjectDTO> subjectsEnrolled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Collection<SubjectDTO> getSubjectsEnrolled() {
        return subjectsEnrolled;
    }

    public void setSubjectsEnrolled(Collection<SubjectDTO> subjectsEnrolled) {
        this.subjectsEnrolled = subjectsEnrolled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(birth, that.birth) &&
                Objects.equals(subjectsEnrolled, that.subjectsEnrolled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birth, subjectsEnrolled);
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birth='" + birth + '\'' +
                ", subjectsEnrolled=" + subjectsEnrolled +
                '}';
    }
}
