package ru.itis.practice.repositories;

import ru.itis.practice.model.Mentor;
import ru.itis.practice.model.Student;
import ru.itis.practice.model.Subject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryJDBC implements StudentRepository{

    //Language = SQL
    private static final String SQL_SELECT_BY_ID = "SELECT student.id AS s_id, mentor.id AS m_id," +
            " student.first_name AS s_fn, student.last_name AS s_ln, mentor.first_name AS m_fn," +
            " mentor.last_name AS m_ln, subject.id AS subj_id, * FROM " +
            "(student LEFT JOIN mentor ON student.id = mentor.student_id) " +
            "LEFT JOIN subject ON mentor.subject_id = subject.id WHERE student.id = %d";
    private static final String SQL_SELECT_BY_AGE = "SELECT student.id AS s_id, mentor.id AS m_id," +
            " student.first_name AS s_fn, student.last_name AS s_ln, mentor.first_name AS m_fn," +
            " mentor.last_name AS m_ln, subject.id AS subj_id, * FROM " +
            "(student LEFT JOIN mentor ON student.id = mentor.student_id) " +
            "LEFT JOIN subject ON mentor.subject_id = subject.id WHERE age = %d " +
            "ORDER BY student.id DESC";
    private static final String SQL_SELECT_ALL = "SELECT student.id AS s_id, mentor.id AS m_id," +
            " student.first_name AS s_fn, student.last_name AS s_ln, mentor.first_name AS m_fn," +
            " mentor.last_name AS m_ln, subject.id AS subj_id, * FROM" +
            " (student LEFT JOIN mentor ON student.id = mentor.student_id) " +
            "LEFT JOIN subject ON mentor.subject_id = subject.id ORDER BY student.id DESC";
    private static final String SQL_INSERT_STUDENT = "INSERT INTO student" +
            "(first_name, last_name, age, group_number) VALUES ('%s', '%s', %d, %d) returning id";
    private static final String SQL_INSERT_MENTOR = "INSERT INTO mentor" +
            "(first_name, last_name, student_id, subject_id) VALUES ('%s', '%s', %d, %d)";
    private static final String SQL_INSERT_MENTOR_EXT = ", ('%s', '%s', %d, %d)";
    private static final String SQL_RETURNING_ID = " returning id";
    private static final String SQL_UPDATE_STUDENT = "UPDATE student SET " +
            "first_name = '%s', last_name = '%s', age = %d, group_number = %d WHERE id = %d";
    private static final String SQL_DELETE_MENTORS = "DELETE FROM mentor WHERE student_id = %d";

    private Connection connection;

    public StudentRepositoryJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Student> findAllByAge(int age) {
        List<Student> result = new ArrayList<>();
        Statement statement = null;
        ResultSet set = null;
        try{
            statement = connection.createStatement();
            set = statement.executeQuery(String.format(SQL_SELECT_BY_AGE, age));
            long lastIndex = -1;
            Student s = new Student(-1,"","",0,0);
            while (set.next()){
                if(lastIndex != set.getLong("s_id")) {
                    s = new Student(
                            set.getLong("s_id"),
                            set.getString("s_fn"),
                            set.getString("s_ln"),
                            set.getInt("age"),
                            set.getInt("group_number")
                    );
                    if(set.getObject("m_id") != null){
                        s.getMentors().add(new Mentor(
                                set.getLong("m_id"),
                                set.getString("m_fn"),
                                set.getString("m_ln"),
                                s,
                                new Subject(set.getLong("subj_id"), set.getString("name"))
                        ));
                    }
                    lastIndex = s.getId();
                    result.add(s);
                }
                else {
                    s.getMentors().add(new Mentor(
                            set.getLong("m_id"),
                            set.getString("m_fn"),
                            set.getString("m_ln"),
                            s,
                            new Subject(set.getLong("subj_id"), set.getString("name"))
                    ));
                    lastIndex = s.getId();
                }
            }
        } catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        finally {
            try {
                if(set != null) {
                    set.close();
                };
            } catch (SQLException throwables) {
                //ignore
            }
            try {
                if(statement != null) {
                    statement.close();
                }
            } catch (SQLException throwables) {
                //ignore
            }
        }
        return result;
    }

    @Override
    public List<Student> findAll() {
        List<Student> result = new ArrayList<>();
        Statement statement = null;
        ResultSet set = null;
        try {
            statement = connection.createStatement();
            set = statement.executeQuery(SQL_SELECT_ALL);
            long lastIndex = -1;
            Student s = new Student(-1, "", "", 0, 0);
            while (set.next()) {
                if (lastIndex != set.getLong("s_id")) {
                    s = new Student(
                            set.getLong("s_id"),
                            set.getString("s_fn"),
                            set.getString("s_ln"),
                            set.getInt("age"),
                            set.getInt("group_number")
                    );
                    if (set.getObject("m_id") != null) {
                        s.getMentors().add(new Mentor(
                                set.getLong("m_id"),
                                set.getString("m_fn"),
                                set.getString("m_ln"),
                                s,
                                new Subject(set.getLong("subj_id"), set.getString("name"))
                        ));
                    }
                    lastIndex = s.getId();
                    result.add(s);
                } else {
                    s.getMentors().add(new Mentor(
                            set.getLong("m_id"),
                            set.getString("m_fn"),
                            set.getString("m_ln"),
                            s,
                            new Subject(set.getLong("subj_id"), set.getString("name"))
                    ));
                    lastIndex = s.getId();
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            try {
                if(set != null) {
                    set.close();
                }
            } catch (SQLException throwables) {
                //ignore
            }
            try {
                if(statement != null) {
                    statement.close();
                }
            } catch (SQLException throwables) {
                //ignore
            }
        }
        return result;
    }

    @Override
    public Student findById(Long id) {
        Statement statement = null;
        ResultSet set = null;
        try{
            statement = connection.createStatement();
            set = statement.executeQuery(String.format(SQL_SELECT_BY_ID, id));
            Student s = new Student(-1,"","",0,0);
            if(set.next()){
                s = new Student(
                        set.getLong("s_id"),
                        set.getString("s_fn"),
                        set.getString("s_ln"),
                        set.getInt("age"),
                        set.getInt("group_number")
                );
                if(set.getObject("m_id") != null){
                    s.getMentors().add(new Mentor(
                            set.getLong("m_id"),
                            set.getString("m_fn"),
                            set.getString("m_ln"),
                            s,
                            new Subject(set.getLong("subj_id"), set.getString("name"))
                    ));
                }
            }
            while (set.next()){
                s.getMentors().add(new Mentor(
                        set.getLong("m_id"),
                        set.getString("m_fn"),
                        set.getString("m_ln"),
                        s,
                        new Subject(set.getLong("subj_id"), set.getString("name"))
                ));
            }
            return s;
        }catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        finally {
            try {
                if(set != null) {
                    set.close();
                }
            } catch (SQLException throwables) {
                //ignore
            }
            try {
                if(statement != null) {
                    statement.close();
                }
            } catch (SQLException throwables) {
                //ignore
            }
        }
    }

    @Override
    public void save(Student entity) {
        Statement statement = null;
        ResultSet set = null;
        try {
            statement = connection.createStatement();
            set = statement.executeQuery(String.format(SQL_INSERT_STUDENT,
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getAge(),
                    entity.getGroup()));
            set.next();
            entity.setId(set.getLong("id"));
            List<Mentor> mentors = entity.getMentors();
            boolean first = true;
            StringBuilder query = null;
            for (Mentor m : mentors) {
                if(first) {
                    query = new StringBuilder(String.format(SQL_INSERT_MENTOR,
                            m.getFirstName(),
                            m.getLastName(),
                            m.getStudent().getId(),
                            m.getSubject().getId()));
                    first = false;
                }
                else {
                    query.append(String.format(SQL_INSERT_MENTOR_EXT,
                            m.getFirstName(),
                            m.getLastName(),
                            m.getStudent().getId(),
                            m.getSubject().getId()));
                }
            }
            if(query != null) {
                query.append(SQL_RETURNING_ID);
                set = statement.executeQuery(query.toString());
            }
            for (int i = 0; i < mentors.size(); i++){
                set.next();
                mentors.get(i).setId(set.getLong("id"));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        finally {
            try {
                if(set != null) {
                    set.close();
                }
            } catch (SQLException throwables) {
                //ignore
            }
            try {
                if(statement != null) {
                    statement.close();
                }
            } catch (SQLException throwables) {
                //ignore
            }
        }
    }

    @Override
    public void update(Student entity) {
        Statement statement = null;
        ResultSet set = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(String.format(SQL_UPDATE_STUDENT,
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getAge(),
                    entity.getGroup(),
                    entity.getId()));
            statement.execute(String.format(SQL_DELETE_MENTORS, entity.getId()));
            List<Mentor> mentors = entity.getMentors();
            boolean first = true;
            StringBuilder query = null;
            for (Mentor m : mentors) {
                if(first) {
                    query = new StringBuilder(String.format(SQL_INSERT_MENTOR,
                            m.getFirstName(),
                            m.getLastName(),
                            m.getStudent().getId(),
                            m.getSubject().getId()));
                    first = false;
                }
                else {
                    query.append(String.format(SQL_INSERT_MENTOR_EXT,
                            m.getFirstName(),
                            m.getLastName(),
                            m.getStudent().getId(),
                            m.getSubject().getId()));
                }
            }
            if(query != null) {
                query.append(SQL_RETURNING_ID);
                set = statement.executeQuery(query.toString());
            }
            for (int i = 0; i < mentors.size(); i++){
                set.next();
                mentors.get(i).setId(set.getLong("id"));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
        finally {
            try {
                if(set != null) {
                    set.close();
                }
            } catch (SQLException throwables) {
                //ignore
            }
            try {
                if(statement != null) {
                    statement.close();
                }
            } catch (SQLException throwables) {
                //ignore
            }
        }
    }
}
