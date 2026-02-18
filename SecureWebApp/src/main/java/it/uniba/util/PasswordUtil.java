package it.uniba.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe di utilità per la gestione sicura delle password.
 * Utilizza l'algoritmo BCrypt per l'hashing e la verifica.
 *
 * @author Matteo Esposito
 * @version 1.0
 */
public class PasswordUtil {

    /**
     * Il fattore di lavoro per la generazione del salt.
     */
    private static final int WORK_FACTOR = 12;

    /**
     * Costruttore privato per impedire l'istanziazione della classe utility.
     */
    private PasswordUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Genera un hash sicuro della password fornita utilizzando BCrypt.
     * Include automaticamente un "salt" casuale all'interno dell'hash restituito.
     *
     * @param plainTextPassword La password in chiaro inserita dall'utente.
     * @return Una stringa contenente l'hash BCrypt,
     * oppure {@code null} se l'input è nullo o vuoto.
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            return null;
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(WORK_FACTOR));
    }

    /**
     * Verifica se una password in chiaro corrisponde a un hash memorizzato.
     * BCrypt estrae il salt dall'hash memorizzato e riesegue l'hashing sulla
     * password in chiaro per verificare la corrispondenza.
     *
     * @param plainTextPassword La password in chiaro da verificare (es. dal form di login).
     * @param hashedPassword L'hash BCrypt recuperato dal database.
     * @return {@code true} se la password corrisponde all'hash;
     * {@code false} se non corrisponde o se uno degli input è nullo.
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}