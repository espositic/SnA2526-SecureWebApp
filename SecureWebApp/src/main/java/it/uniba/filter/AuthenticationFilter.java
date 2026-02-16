package it.uniba.filter;

import it.uniba.dao.UserDAO;
import it.uniba.model.User;
import it.uniba.util.CookieUtil;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtro di sicurezza principale (Gatekeeper).
 * Intercetta ogni richiesta verso l'applicazione per garantire che solo
 * gli utenti autenticati possano accedere alle risorse protette.
 *
 * <p>Funzionalità principali:
 * <ul>
 * <li>Applicazione Security Headers (No-Cache).</li>
 * <li>Gestione Whitelist per risorse pubbliche (Login, CSS, JS).</li>
 * <li>Gestione eccezioni per upload (Multipart Request).</li>
 * <li>Verifica Sessione e Login Automatico (Remember Me).</li>
 * </ul>
 * </p>
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // ========================================================================
        // SECURITY HEADERS
        // ========================================================================
        // Impediamo al browser di memorizzare nella cache le pagine protette.
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // ========================================================================
        // WHITELIST
        // ========================================================================
        // Definiamo quali percorsi sono accessibili a tutti.
        boolean isStatic = path.contains("/css/") || path.contains("/js/") || path.contains("/img/");
        boolean isPublic = path.endsWith("/login.jsp") || path.endsWith("/login") ||
                path.endsWith("/register.jsp") || path.endsWith("/register");

        // ECCEZIONE IMPORTANTE: Le Servlet di "Azione" (Upload, Logout) devono passare.
        // Le richieste Multipart (file upload) sono complesse; se le blocchiamo qui,
        // rischiamo di perdere i dati della sessione. Deleghiamo il controllo alla Servlet.
        boolean isAction = path.endsWith("/upload") || path.endsWith("/logout") || path.endsWith("/feed");

        // ========================================================================
        // CONTROLLO SESSIONE
        // ========================================================================
        // Recuperiamo la sessione esistente.
        HttpSession session = httpRequest.getSession(false);

        // Verifichiamo che l'attributo "user" esista e sia del tipo corretto.
        boolean isLoggedIn = (session != null && session.getAttribute("user") instanceof User);

        if (isLoggedIn || isPublic || isStatic || isAction) {
            chain.doFilter(request, response);
            return;
        }

        // ========================================================================
        // REMEMBER ME
        // ========================================================================
        // Se non siamo loggati, cerchiamo il cookie remember me.
        String cookieValue = getCookieValue(httpRequest, CookieUtil.COOKIE_NAME);
        if (cookieValue != null) {
            // Verifichiamo la firma crittografica (HMAC) del cookie
            String userEmail = CookieUtil.verifyAndGetValue(cookieValue);

            if (userEmail != null) {
                // Cookie valido: Recuperiamo l'utente completo dal Database
                UserDAO dao = new UserDAO();
                User user = dao.getUserByEmail(userEmail);

                if (user != null) {
                    // Ripristiniamo la sessione con l'oggetto User completo
                    session = httpRequest.getSession(true);
                    session.setAttribute("user", user);
                    chain.doFilter(request, response);
                    return;
                }
            }
            // Se il cookie è invalido o l'utente non esiste più, puliamo tutto.
            CookieUtil.deleteCookie(httpResponse, CookieUtil.COOKIE_NAME);
        }

        // ========================================================================
        // ACCESSO NEGATO
        // ========================================================================
        // Nessuna credenziale valida trovata. Reindirizzamento al login.
        httpResponse.sendRedirect(contextPath + "/login.jsp");
    }

    /**
     * Helper per estrarre il valore di un cookie specifico.
     */
    private String getCookieValue(HttpServletRequest req, String name) {
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if (name.equals(c.getName())) return c.getValue();
            }
        }
        return null;
    }

    @Override public void init(FilterConfig f) {}
    @Override public void destroy() {}
}