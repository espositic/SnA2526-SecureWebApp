package it.uniba.controller;

import it.uniba.dao.UserDAO;
import it.uniba.model.User;
import it.uniba.util.CookieUtil;
import it.uniba.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet responsabile dell'autenticazione degli utenti.
 * Gestisce il ciclo di vita del login:
 * <ol>
 * <li>Ricezione credenziali (email/password).</li>
 * <li>Verifica crittografica della password (BCrypt).</li>
 * <li>Creazione della Sessione HTTP sicura.</li>
 * <li>Generazione del cookie "Remember Me" firmato tramite CookieUtil.</li>
 * </ol>
 *
 * @author Matteo Esposito
 * @version 1.0
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /**
     * Gestisce la richiesta POST per il login.
     *
     * @param request  La richiesta HTTP contenente i parametri del form.
     * @param response La risposta HTTP per il redirect o l'errore.
     * @throws ServletException In caso di errori interni del server.
     * @throws IOException      In caso di errori di I/O.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Recupero parametri dal form HTML
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        // Interazione con il Database
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(email);

        // Verifica delle Credenziali tramite PasswordUtil
        if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) {

            // ====================================================================
            // GESTIONE SESSIONE (Session Management)
            // ====================================================================
            HttpSession session = request.getSession(true);

            // Impostiamo un timeout di inattività di 30 minuti.
            session.setMaxInactiveInterval(30 * 60);

            // Salviamo l'oggetto User in sessione per l'accesso globale ai dati utente.
            session.setAttribute("user", user);

            // ====================================================================
            // GESTIONE REMEMBER ME (Cookie Persistente con HMAC)
            // ====================================================================
            if (rememberMe != null && rememberMe.equals("on")) {
                // Utilizziamo la classe CookieUtil per generare un cookie sicuro.
                // Il metodo addCookie gestisce internamente:
                // - Firma HMAC-SHA256 del valore (email)
                // - Flags HttpOnly e SameSite=Lax
                // - Flag Secure (in base alla configurazione ambientale)

                int durata = 7 * 24 * 60 * 60; // 7 Giorni in secondi

                CookieUtil.addCookie(
                        response,
                        CookieUtil.COOKIE_NAME,
                        user.getEmail(),
                        durata
                );
            }

            // 6. Redirect alla pagina del feed (o home)
            response.sendRedirect("home");

        } else {
            // Login Fallito: Messaggio di errore e ritorno alla pagina di login
            request.setAttribute("errorMessage", "Email o Password errati");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}