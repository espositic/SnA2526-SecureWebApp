package it.uniba.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Genera l'hash sicuro con costo 12.
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    /**
     * Verifica la password.
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
