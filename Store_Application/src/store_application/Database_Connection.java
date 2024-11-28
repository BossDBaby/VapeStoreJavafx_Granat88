package store_application;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database_Connection {
    public Connection databaseLink;
    
    public Connection getConnection() {
        String databaseName = "";
        String databaseUser = "";
        String databasePassword = "";
        String url = "jbdc:mysql://localhost/VaporG";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver" + databaseName);
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        
        return databaseLink;
    }
}
