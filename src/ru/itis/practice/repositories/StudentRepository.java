package ru.itis.practice.repositories;

import ru.itis.practice.model.Student;

import java.util.List;

public interface StudentRepository extends Repository<Student>{
    List<Student> findAllByAge(int age);
}
