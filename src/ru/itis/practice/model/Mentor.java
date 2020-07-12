package ru.itis.practice.model;

public class Mentor {
    private long id;
    private String firstName;
    private String lastName;
    private Student student;
    private Subject subject;

    public Mentor(long id, String firstName, String lastName, Student student, Subject subject) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.student = student;
        this.subject = subject;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
