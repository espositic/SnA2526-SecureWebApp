package it.uniba.filter;

import it.uniba.dao.UserDAO;
import it.uniba.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtro di sicurezza principale (Gatekeeper).
 * Centralizza l'autenticazione: le Servlet protette vengono raggiunte
 * SOLO se questo filtro valida la sessione o il cookie.
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. SECURITY HEADERS
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // 2. WHITELIST (Risorse Pubbliche)
        // Definiamo quali percorsi sono accessibili SENZA login.
        boolean isStatic = path.contains("/css/") || path.contains("/js/") || path.contains("/img/");
        boolean isPublic = path.endsWith("/login.jsp") || path.endsWith("/login") ||
                path.endsWith("/register.jsp") || path.endsWith("/register");

        // NOTA: Abbiamo rimosso 'isAction' (/upload, /feed, /logout).
        // Ora quelle risorse sono considerate PROTETTE di default.

        // 3. CONTROLLO SESSIONE
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") instanceof User);

        // LOGICA DI ACCESSO:
        // Se sei loggato OPPURE la risorsa è pubblica/statica -> Passa
        if (isLoggedIn || isPublic || isStatic) {
            chain.doFilter(request, response);
            return;
        }

        // 5. ACCESSO NEGATO
        // Se arriviamo qui, l'utente sta cercando di accedere a una risorsa privata (es. /feed)
        // senza sessione valida.
        httpResponse.sendRedirect(contextPath + "/login.jsp?msg=Sessione scaduta o accesso non autorizzato.");
    }

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