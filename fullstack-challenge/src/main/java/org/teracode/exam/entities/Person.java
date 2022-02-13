package org.teracode.exam.entities;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Person {
    @Id
    @GeneratedValue
    protected Long id;
    String firstName;
    String lastName;
    LocalDate birth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public final String getFirstName() {
        return firstName;
    }

    public final void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public final String getLastName() {
        return lastName;
    }

    public final void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public final LocalDate getBirth() {
        return birth;
    }

    public final void setBirth(LocalDate birth) {
        this.birth = birth;
    }
}
