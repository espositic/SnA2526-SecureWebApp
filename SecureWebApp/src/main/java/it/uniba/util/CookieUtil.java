package it.uniba.util;

import javax.servlet.http.HttpServletResponse;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CookieUtil {

    public static final String COOKIE_NAME = "SECURE_SESSION_ID";
    private static final String SECRET_KEY = "vK7s9Lp2!Xz@5dQ#9mR$8wB&1nJ*3gH%4fK^7tY(0cU)6iO-2aE+5vM";

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        String signature = calculateHMAC(value);
        String tokenSigned = value + "." + signature;
        String cookieHeader = String.format(
                "%s=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=Lax",
                name,
                tokenSigned,
                maxAge
        );

        response.addHeader("Set-Cookie", cookieHeader);
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        String cookieHeader = String.format(
                "%s=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=Lax",
                name
        );

        response.addHeader("Set-Cookie", cookieHeader);
    }

    private static String calculateHMAC(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(CookieUtil.SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] rawHmac = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Errore calcolo HMAC", e);
        }
    }

    public static String verifyAndGetValue(String cookieValue) {
        if (cookieValue == null || !cookieValue.contains(".")) return null;

        String[] parts = cookieValue.split("\\.");
        if (parts.length != 2) return null;

        String originalValue = parts[0];
        String receivedSignature = parts[1];

        String expectedSignature = calculateHMAC(originalValue);


        if (expectedSignature.equals(receivedSignature)) {
            return originalValue;
        } else {
            return null;
        }
    }
}