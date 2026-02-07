package it.uniba.util;

public class SecurityUtil {

    /**
     * Previene Stored XSS trasformando i caratteri speciali in entità HTML.
     */
    public static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        // Sostituzione dei 5 caratteri critici per l'HTML
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}