/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.sql.*;

/**
 *
 * @author huyho
 */
public class ConnectionDatabase {
    Connection conn = null;
    private static String database;
    private static String username;
    private static String password;
    public Connection getConnect() {
        // lay chuoi vua cat gan qua cho bien moi de thuc hien ket noi
        database = ServerThread.database;
        username = ServerThread.username;
        password = ServerThread.password;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://127.0.0.1:1433;" + "databaseName=" + database + ";user=" + username + ";password=" + password;
            System.out.println(url);
            conn = DriverManager.getConnection(url);
            System.out.println("Da ket noi voi database " + conn.getCatalog());
            return conn;
        } catch (Exception e) {
            return null;
        }
    }
}
