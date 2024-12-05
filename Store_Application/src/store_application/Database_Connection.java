package store_application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database_Connection {
    private static final String DATABASE_URL = "jdbc:mysql://localhost/VaporG?useSSL=false&serverTimezone=UTC";
    private static final String DATABASE_USER = "root"; // Ganti dengan username MySQL Anda
    private static final String DATABASE_PASSWORD = ""; // Ganti dengan password MySQL Anda

    public Connection getConnection() {
        Connection connection = null;
        
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            System.out.println("Koneksi berhasil!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Koneksi gagal!");
            e.printStackTrace();
        }
        
        return connection;
    }
}
