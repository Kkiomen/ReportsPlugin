package me.kkiomen.pl;

import java.sql.*;
import java.util.UUID;

public class MYSQL {

    final String username="root"; // Enter in your db username
    final String password=""; // Enter your password for the db
    final String url = "jdbc:mysql://localhost:3306/websitemc"; // Enter URL with db name

    //Connection vars
    public static Connection connection; //This is the variable we will use to connect to database

    public void connect(){
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "CREATE TABLE IF NOT EXISTS `reports` ( " +
                "`id` INT NOT NULL AUTO_INCREMENT , " +
                "`nick` TEXT NOT NULL , " +
                "`text` TEXT NOT NULL , " +
                "PRIMARY KEY (`id`)) ENGINE = InnoDB;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            // use executeUpdate() to update the databases table.
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void close(){
        try {
            if (connection!=null && !connection.isClosed()){

                connection.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }




}
