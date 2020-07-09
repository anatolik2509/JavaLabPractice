package ru.itis.practice;

import java.sql.*;

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
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("select * from student");
            while (set.next()){
                System.out.println("ID: " + set.getString("id"));
                System.out.println("First Name: " + set.getString("first_name"));
                System.out.println("Last Name: " + set.getString("last_name"));
                System.out.println("Age: " + set.getInt("age"));
                System.out.println("Group: " + set.getInt("group_number"));
            }
            set.close();
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throw new IllegalArgumentException();
        }
    }
}
