package it.uniba.controller;

import it.uniba.dao.UserDAO;
import it.uniba.model.User;
import it.uniba.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Servlet responsabile dell'autenticazione degli utenti.
 * Gestisce il ciclo di vita del login:
 * <ol>
 * <li>Ricezione credenziali (email/password).</li>
 * <li>Verifica crittografica della password (BCrypt).</li>
 * <li>Creazione della Sessione HTTP sicura.</li>
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

        // Recupero parametri dal form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Interazione con il Database
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(email);

        // Verifica delle Credenziali tramite PasswordUtil
        if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) {

            // ====================================================================
            // GESTIONE SESSIONE
            // ====================================================================
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);

            // Impostiamo un timeout di inattività di 30 minuti.
            session.setMaxInactiveInterval(30 * 60);

            // Salviamo l'oggetto User in sessione per l'accesso globale ai dati utente.
            session.setAttribute("user", user);

            // 6. Redirect alla pagina del feed (o home)
            response.sendRedirect("home.jsp");

        } else {
            // In caso di Login fallito, messaggio di errore e ritorno alla pagina di login
            request.setAttribute("errorMessage", "Email o Password errati");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}