package it.uniba.util;

import java.util.regex.Pattern;

/**
 * Classe di utilità per la validazione dei dati di input.
 * Fornisce metodi statici per verificare la conformità di stringhe
 * rispetto a pattern predefiniti.
 *
 * @author Matteo Esposito
 * @version 1.0
 */
public class ValidationUtil {

    /**
     * Espressione regolare per la validazione del formato email.
     * Segue lo standard generale per username e dominio.
     */
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * Pattern pre-compilato per l'email.
     * Mantenere il pattern compila<to come costante migliora le prestazioni
     * evitando la ricompilazione ad ogni chiamata del metodo.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Espressione regolare per la validazione della password.
     * Requisiti:
     * <ul>
     * <li>Minimo 8 caratteri</li>
     * <li>Almeno 1 lettera maiuscola</li>
     * <li>Almeno 1 lettera minuscola</li>
     * <li>Almeno 1 numero</li>
     * <li>Almeno 1 carattere speciale (@#$%^&+=!._-)</li>
     * <li>Nessuno spazio bianco</li>
     * </ul>
     */
    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._-])(?=\\S+$).{8,}$";

    /**
     * Pattern pre-compilato per la password.
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    /**
     * Costruttore privato per impedire l'istanziazione della classe utility.
     */
    private ValidationUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Verifica se il formato della stringa fornita corrisponde a un'email valida.
     *
     * @param email La stringa contenente l'indirizzo email da validare.
     * @return {@code true} se l'email non è nulla e rispetta il pattern regex;
     * {@code false} altrimenti.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Verifica se la password fornita rispetta i criteri di complessità e sicurezza.
     *
     * @param password La stringa contenente la password da validare.
     * @return {@code true} se la password non è nulla e soddisfa tutti i requisiti di complessità;
     * {@code false} altrimenti.
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}