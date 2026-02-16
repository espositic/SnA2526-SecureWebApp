package it.uniba.util;

import javax.servlet.http.HttpServletResponse;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Classe di utilità per la gestione sicura dei Cookie.
 * Implementa meccanismi di firma HMAC per prevenire la manipolazione dei cookie
 * lato client.
 *
 * <p>Configurazione richiesta nelle Variabili d'Ambiente:
 * <ul>
 * <li>{@code SECRET_KEY}: Chiave segreta per la firma HMAC (minimo 32 caratteri).</li>
 * <li>{@code COOKIE_SECURE}: "true".</li>
 * </ul>
 * </p>
 *
 * @author Matteo Esposito
 * @version 1.0
 */
public class CookieUtil {

    /**
     * Nome standard del cookie per la funzionalità "Ricordami".
     */
    public static final String COOKIE_NAME = "REMEMBER_ME_COOKIE";

    /**
     * Algoritmo di hashing utilizzato per la firma (HMAC-SHA256).
     */
    private static final String HMAC_ALGO = "HmacSHA256";

    // ==================================================================================
    // CONFIGURAZIONE AMBIENTALE
    // ==================================================================================

    /**
     * Chiave segreta per la firma.
     * Viene caricata dall'ambiente.
     */
    private static final String SECRET_KEY = getRequiredEnv("SECRET_KEY");

    /**
     * Flag per determinare se il cookie deve avere l'attributo "Secure".
     * In produzione (HTTPS) DEVE essere true. In locale (HTTP) può essere false.
     */
    private static final boolean IS_SECURE_COOKIE = Boolean.parseBoolean(System.getenv("COOKIE_SECURE"));

    /**
     * Costruttore privato per impedire l'istanziazione.
     */
    private CookieUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Crea un cookie firmato e lo aggiunge alla risposta HTTP.
     * Il valore del cookie sarà nel formato: {@code VALORE.FIRMA}
     *
     * @param response L'oggetto HttpServletResponse.
     * @param name Il nome del cookie.
     * @param value Il valore da memorizzare (es. username o token).
     * @param maxAge La durata del cookie in secondi.
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        try {
            // Calcola la firma HMAC del valore
            String signature = calculateHMAC(value);

            // Concatena valore e firma
            String tokenSigned = value + "." + signature;

            // Costruisce l'header Set-Cookie manualmente per supportare SameSite
            StringBuilder cookieHeader = new StringBuilder();
            cookieHeader.append(name).append("=").append(tokenSigned).append("; ");
            cookieHeader.append("Path=/; ");
            cookieHeader.append("Max-Age=").append(maxAge).append("; ");
            cookieHeader.append("HttpOnly; ");
            cookieHeader.append("SameSite=Lax");

            // Aggiunge il flag Secure solo se l'ambiente lo prevede (HTTPS)
            if (IS_SECURE_COOKIE) {
                cookieHeader.append("; Secure");
            }

            response.addHeader("Set-Cookie", cookieHeader.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invalida un cookie impostando la sua durata a 0.
     *
     * @param response L'oggetto HttpServletResponse.
     * @param name Il nome del cookie da cancellare.
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        StringBuilder cookieHeader = new StringBuilder();
        cookieHeader.append(name).append("=; ");
        cookieHeader.append("Path=/; ");
        cookieHeader.append("Max-Age=0; ");
        cookieHeader.append("HttpOnly; ");
        cookieHeader.append("SameSite=Lax");

        if (IS_SECURE_COOKIE) {
            cookieHeader.append("; Secure");
        }

        response.addHeader("Set-Cookie", cookieHeader.toString());
    }

    /**
     * Verifica l'integrità del cookie e ne restituisce il valore originale.
     * Ricalcola la firma del valore estratto e la confronta con quella ricevuta.
     *
     * @param cookieValue Il valore completo del cookie (formato {@code VALORE.FIRMA}).
     * @return Il valore originale se la firma è valida; {@code null} se il cookie è compromesso o malformato.
     */
    public static String verifyAndGetValue(String cookieValue) {
        if (cookieValue == null || !cookieValue.contains(".")) {
            return null;
        }

        // Si assume che la firma sia l'ultima parte dopo l'ultimo punto.
        int lastDotIndex = cookieValue.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == cookieValue.length() - 1) {
            return null;
        }

        String originalValue = cookieValue.substring(0, lastDotIndex);
        String receivedSignature = cookieValue.substring(lastDotIndex + 1);

        String expectedSignature = calculateHMAC(originalValue);

        // SICUREZZA: Utilizza MessageDigest.isEqual per il confronto.
        // Questo previene "Timing Attacks", poiché il tempo di confronto è costante
        // indipendentemente da quanti caratteri sono corretti.
        if (expectedSignature != null && MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                receivedSignature.getBytes(StandardCharsets.UTF_8))) {
            return originalValue;
        } else {
            return null;
        }
    }

    /**
     * Calcola l'HMAC-SHA256 di una stringa.
     *
     * @param data I dati da firmare.
     * @return La firma codificata in Base64 URL-safe.
     */
    private static String calculateHMAC(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec secret_key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
            sha256_HMAC.init(secret_key);

            byte[] rawHmac = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Usa Base64 URL Encoder per evitare caratteri non validi nei cookie (+, /)
            return Base64.getUrlEncoder().withoutPadding().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Errore critico nel calcolo HMAC", e);
        }
    }

    /**
     * Recupera una variabile d'ambiente obbligatoria.
     *
     * @param key Il nome della variabile.
     * @return Il valore della variabile.
     * @throws IllegalStateException Se la variabile manca.
     */
    private static String getRequiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Errore configurazione sicurezza: Variabile '" + key + "' mancante.");
        }
        return value;
    }
}