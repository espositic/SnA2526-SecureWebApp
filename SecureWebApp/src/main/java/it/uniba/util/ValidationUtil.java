package it.uniba.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    // Regex Email
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // Regex Password: Minimo di 8 caratteri,di cui 1 maiuscolo, 1 minuscolo,
    // 1 numero e carattere speciale
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._-])(?=\\S+$).{8,}$";

    /**
     * Controlla solo se il formato della mail è valido.
     */
    public static boolean isValidEmail(String email) {
        return email != null && Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    /**
     * Controlla solo se la complessità della password è rispettata.
     */
    public static boolean isValidPassword(String password) {
        return password != null && Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }
}
