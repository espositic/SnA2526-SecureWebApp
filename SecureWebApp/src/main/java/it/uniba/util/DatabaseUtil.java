package it.uniba.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    // Parametri coincidenti con il file .env / docker-compose
    private static final String URL = "jdbc:mysql://db:3306/secure_app_db?useSSL=true&requireSSL=true&allowPublicKeyRetrieval=true";
    private static final String USER = "app_user";
    private static final String PASSWORD = "AppUserSecretPassword!2025";

    static {
        try {
            // Caricamento del driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL non trovato. Verifica il pom.xml", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // Concatena URL base + Parametri SSL
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}