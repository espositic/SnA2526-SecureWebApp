package it.uniba.controller;

import it.uniba.util.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet per la gestione del Logout.
 * Si occupa di terminare la sessione lato server e di invalidare
 * i cookie lato client.
 *
 * @author Matteo Esposito
 * @version 1.0
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Gestisce la richiesta di logout.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // ========================================================================
        // INVALIDAZIONE SESSIONE SERVER
        // ========================================================================
        // Recupera la sessione corrente, se esiste, senza crearne una nuova.
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Cancella tutti i dati salvati in sessione (attributi "user", "userId", ecc.)
            // e la rende non più utilizzabile.
            session.invalidate();
            System.out.println("LogoutServlet: Sessione server invalidata con successo.");
        }

        // ========================================================================
        // PULIZIA COOKIE CLIENT
        // ========================================================================

        // Cancelliamo il cookie "Remember Me".
        CookieUtil.deleteCookie(response, CookieUtil.COOKIE_NAME);

        // Cancelliamo anche il cookie di sessione standard del Container (Tomcat).
        CookieUtil.deleteCookie(response, "JSESSIONID");

        // ========================================================================
        // REDIRECT
        // ========================================================================
        // Rimandiamo l'utente al login con un parametro per mostrare un messaggio di conferma.
        response.sendRedirect("login.jsp?logout=true");
    }
}