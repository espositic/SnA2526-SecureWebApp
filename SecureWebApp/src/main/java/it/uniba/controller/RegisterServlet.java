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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        String errorMessage = null;

        // Validazione
        if (!ValidationUtil.isValidEmail(email)) {
            errorMessage = "Formato email non valido.";
        } else if (!ValidationUtil.isValidPassword(password)) {
            errorMessage = "La password non rispetta i requisiti di sicurezza.";
        } else if (!password.equals(confirmPassword)) {
            errorMessage = "Le password non coincidono.";
        }

        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "Impossibile completare la registrazione. Verifica i dati inseriti.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String passwordHash = PasswordUtil.hashPassword(password);

        User newUser = new User(email, passwordHash);

        if (userDAO.registerUser(newUser)) {
            response.sendRedirect("login.jsp?registered=true");
        } else {
            request.setAttribute("error", "Errore di sistema.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}