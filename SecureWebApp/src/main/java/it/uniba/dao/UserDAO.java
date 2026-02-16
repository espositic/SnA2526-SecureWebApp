package it.uniba.dao;

import it.uniba.model.User;
import it.uniba.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) per la gestione dell'entità Utente.
 * Fornisce i metodi per interagire con la tabella 'users' del database,
 * gestendo le operazioni di lettura (Login) e scrittura (Registrazione).
 *
 * @author Matteo Esposito
 * @version 1.0
 */
public class UserDAO {

    /**
     * Cerca un utente nel database tramite il suo indirizzo email.
     * Utilizzato principalmente dalla LoginServlet per verificare le credenziali.
     *
     * @param email L'indirizzo email da cercare (univoco).
     * @return Un oggetto {@link User} popolato se l'utente esiste, altrimenti {@code null}.
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    // Mapping dei risultati della query sull'oggetto Java
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    // Recuperiamo l'hash della password per la verifica
                    user.setPasswordHash(rs.getString("password_hash"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verifica se un determinato indirizzo email è già registrato nel sistema.
     * Utilizzato dalla RegisterServlet per prevenire la creazione di account duplicati.
     *
     * @param email L'email da controllare.
     * @return {@code true} se l'email è già presente nel DB, {@code false} altrimenti.
     */
    public boolean emailExists(String email) {
        // Riutilizza il metodo di ricerca per verificare l'esistenza
        return getUserByEmail(email) != null;
    }

    /**
     * Registra un nuovo utente nel database.
     * Inserisce l'email e l'hash della password nella tabella utenti.
     *
     * @param user L'oggetto {@link User} contenente i dati da salvare.
     * @return {@code true} se la registrazione ha successo, {@code false} in caso di errore SQL.
     */
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (email, password_hash) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getEmail());
            // Salvataggio sicuro: salviamo solo l'hash, mai la password in chiaro
            pstmt.setString(2, user.getPasswordHash());

            // Esegue l'aggiornamento e ritorna true se almeno una riga è stata creata
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}