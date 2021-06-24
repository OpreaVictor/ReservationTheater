package dao;

import java.sql.*;

public class DbQueries {
    public static ResultSet selectQuery(Connection conn, String query){
        try {
            Statement statement = conn.createStatement();

            return statement.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

            return null;
        }
    }

    public static void insertQuery(Connection conn, String query){
        try {
            Statement statement = conn.createStatement();

            statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
