package it.uniba.controller;

import it.uniba.dao.UserDAO;
import it.uniba.model.User;
import it.uniba.util.CookieUtil;
import it.uniba.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userDAO.getUserByEmail(email);

        if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) {

            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession = request.getSession(true);
            newSession.setMaxInactiveInterval(30 * 60);

            byte[] randomBytes = new byte[32];
            secureRandom.nextBytes(randomBytes);
            String secureToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
            newSession.setAttribute("secureToken", secureToken);
            newSession.setAttribute("user", user);

            CookieUtil.addCookie(response, CookieUtil.COOKIE_NAME, secureToken, 30 * 60);

            response.sendRedirect("home.jsp");

        } else {
            request.setAttribute("error", "Credenziali non valide.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("home.jsp");
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}