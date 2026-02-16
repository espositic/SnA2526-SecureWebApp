package it.uniba.controller;

import it.uniba.dao.UserDAO;
import it.uniba.model.User;
import it.uniba.util.PasswordUtil;
import it.uniba.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet che gestisce il processo di registrazione di nuovi utenti.
 * Si occupa di validare i dati di input, verificare la disponibilità dell'email,
 * crittografare la password e salvare il nuovo utente nel database.
 *
 * @author Matteo Esposito
 * @version 1.0
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    /**
     * DAO per l'accesso ai dati utente.
     * Viene istanziato una volta per servlet (stateless).
     */
    private final UserDAO userDAO = new UserDAO();

    /**
     * Gestisce l'invio del modulo di registrazione (POST).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupero parametri
        String email = request.getParameter("email") != null ? request.getParameter("email").trim() : null;
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        String errorMessage = null;

        // Validazione Input
        if (!ValidationUtil.isValidEmail(email)) {
            errorMessage = "Il formato dell'email non è valido.";
        }
        else if (!ValidationUtil.isValidPassword(password)) {
            errorMessage = "La password deve contenere almeno 8 caratteri, una maiuscola, un numero e un carattere speciale.";
        }
        // Controllo corrispondenza password
        else if (password == null || !password.equals(confirmPassword)) {
            errorMessage = "Le password inserite non coincidono.";
        }

        // Se c'è un errore di validazione formale, blocchiamo subito.
        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
            // Manteniamo l'email inserita per non costringere l'utente a riscriverla
            request.setAttribute("emailValue", email);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Verifica esistenza Utente
        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "Impossibile completare la registrazione con i dati forniti.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Hashing della Password
        String passwordHash = PasswordUtil.hashPassword(password);

        // Creazione e Salvataggio Utente
        User newUser = new User(email, passwordHash);

        if (userDAO.registerUser(newUser)) {
            // Redirect al login con flag per messaggio di cortesia
            response.sendRedirect("login.jsp?registered=true");
        } else {
            // Errore Database
            request.setAttribute("error", "Errore di sistema temporaneo. Riprova più tardi.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    /**
     * Gestisce l'accesso diretto alla pagina di registrazione (GET).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}