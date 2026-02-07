package it.uniba.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final boolean HTTP_ONLY = true;
    private static final boolean SECURE = true; // Mettere a false se non usi HTTPS

    /**
     * Crea un cookie standard
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(HTTP_ONLY);
        cookie.setSecure(SECURE);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * Cancella il cookie usando il Context Path
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        String contextPath = request.getContextPath();

        if (contextPath == null || contextPath.isEmpty()) {
            contextPath = "/";
        }

        Cookie cookie = new Cookie(name, "");
        cookie.setPath(contextPath);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(HTTP_ONLY);
        cookie.setSecure(SECURE);

        response.addCookie(cookie);
    }
}