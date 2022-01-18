package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class DBConnection {
    private static String password = "password";
    private static String username = "root";
    private static Connection con ;

    public static Connection getConnection() throws SQLException {

        Connection con = DriverManager.getConnection(String.format("jdbc:mysql://localhost:3306/users"),username,password);

        return con;

    }
}
