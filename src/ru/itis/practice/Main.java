package ru.itis.practice;

import ru.itis.practice.model.Mentor;
import ru.itis.practice.model.Student;
import ru.itis.practice.model.Subject;
import ru.itis.practice.repositories.StudentRepository;
import ru.itis.practice.repositories.StudentRepositoryJDBC;

import java.sql.*;
import java.util.Collections;
import java.util.List;

public class Main {

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/itis_practice";
    public static final String LOGIN = "postgres";
    public static final String PASSWORD = "itis2020";

    public static Connection openConnection(String url, String login, String password){
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            return connection;
        } catch (SQLException throwables) {
            throw new IllegalArgumentException();
        }
    }


    public static void main(String[] args) {
        Connection connection = openConnection(DB_URL, LOGIN, PASSWORD);
        StudentRepository repository = new StudentRepositoryJDBC(connection);
        Student s = new Student( 11, "Полиграф", "Полиграфович", 18, 901);
        Mentor m1 = new Mentor(0, "bababa", "bebebe", s, new Subject(2, "Квантовая физика"));
        Mentor m2 = new Mentor(0, "fjkfjf", "nbhgkg", s, new Subject(1, "Алхимия"));
        s.getMentors().add(m1);
        s.getMentors().add(m2);
        repository.update(s);
        System.out.println(s);
    }
}
