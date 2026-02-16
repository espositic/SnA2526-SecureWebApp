package it.uniba.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe di utilità per la gestione della connessione al Database.
 *
 * @author Matteo Esposito
 * @version 2.0
 */
public class DatabaseUtil {

    // ==================================================================================
    // CONFIGURAZIONE AMBIENTALE OBBLIGATORIA
    // L'applicazione fallirà l'avvio se queste variabili non sono definite nell'ambiente.
    // ==================================================================================

    /**
     * Host del database.
     * Variabile ambiente: DB_HOST
     */
    private static final String DB_HOST = getRequiredEnv("DB_HOST");

    /**
     * Porta del database.
     * Variabile ambiente: DB_PORT
     */
    private static final String DB_PORT = getRequiredEnv("DB_PORT");

    /**
     * Nome dello schema del database.
     * Variabile ambiente: DB_NAME
     */
    private static final String DB_NAME = getRequiredEnv("DB_NAME");

    /**
     * Username per l'autenticazione.
     * Variabile ambiente: DB_USER
     */
    private static final String DB_USER = getRequiredEnv("DB_USER");

    /**
     * Password per l'autenticazione.
     * Variabile ambiente: DB_PASSWORD
     */
    private static final String DB_PASSWORD = getRequiredEnv("DB_PASSWORD");

    /**
     * Opzioni JDBC fisse per la sicurezza (SSL) e compatibilità.
     * Queste rimangono costanti poiché sono requisiti strutturali dell'applicazione.
     */
    private static final String DB_OPTIONS = "?useSSL=true&requireSSL=true&allowPublicKeyRetrieval=true";

    /**
     * URL JDBC costruito dinamicamente all'avvio della classe.
     */
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + DB_OPTIONS;

    /**
     * Blocco di inizializzazione statico per il driver JDBC.
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Errore critico: Driver MySQL non trovato nel classpath.", e);
        }
    }

    /**
     * Costruttore privato per impedire l'istanziazione.
     */
    private DatabaseUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Ottiene una connessione al database utilizzando le variabili d'ambiente.
     *
     * @return Una connessione attiva {@link Connection}.
     * @throws SQLException Se la connessione fallisce.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Recupera una variabile d'ambiente obbligatoria.
     *
     * @param key Il nome della variabile d'ambiente (es. "DB_USER").
     * @return Il valore della variabile.
     * @throws IllegalStateException Se la variabile d'ambiente manca.
     */
    private static String getRequiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Errore di configurazione: La variabile d'ambiente '" + key + "' è mancante o vuota.");
        }
        return value;
    }
}