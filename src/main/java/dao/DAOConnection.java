package dao;

import java.sql.*;

public class DAOConnection {
    private static Connection connection;

    private DAOConnection(){}

    public static Connection getConnection() {
        if (connection == null) {
            createDbConnection();
        }

        return connection;
    }

    public static void closeConnection(){
        try {
            connection.close();
            connection = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void createDbConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mytest123?user=root&password=1q2w3e4r");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}