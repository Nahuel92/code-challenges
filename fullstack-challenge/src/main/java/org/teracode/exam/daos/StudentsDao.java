package org.teracode.exam.daos;

import org.teracode.exam.entities.Student;

import java.util.Collection;

public interface StudentsDao {
    Collection<Student> getStudents();
}
