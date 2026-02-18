package it.uniba.dao;

import it.uniba.model.Upload;
import it.uniba.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) per la gestione dei file caricati.
 * Questa classe si occupa di interagire direttamente con il database per
 * persistere e recuperare le informazioni relative agli upload.
 *
 * <p>Funzionalità principali:
 * <ul>
 * <li>Salvataggio dei metadati (utente e nome file) nel DB.</li>
 * <li>Recupero dello storico degli upload (feed).</li>
 * </ul>
 * </p>
 *
 * @author Matteo Esposito
 * @version 1.0
 */
public class UploadDAO {

    /** Query per inserire un nuovo record di upload. */
    private static final String SQL_INSERT = "INSERT INTO uploads (user_id, filename) VALUES (?, ?)";

    /**
     * Salva i riferimenti di un nuovo upload nel database.
     * Utilizza un PreparedStatement per garantire la sicurezza contro SQL Injection.
     *
     * @param userId L'ID univoco dell'utente che ha effettuato l'upload.
     * @param filename Il nome del file salvato fisicamente sul server.
     * @return {@code true} se l'inserimento ha avuto successo, {@code false} in caso di errore.
     */
    public boolean saveUpload(int userId, String filename) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT)) {

            // Impostazione dei parametri della query sicura
            pstmt.setInt(1, userId);
            pstmt.setString(2, filename);

            // Esecuzione dell'update e verifica delle righe modificate
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Log dell'errore (in produzione si userebbe un Logger slf4j/log4j)
            e.printStackTrace();
            return false;
        }
    }
}