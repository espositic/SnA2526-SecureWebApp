package it.uniba.controller;

import it.uniba.model.User;
import it.uniba.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class UploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();

        if (fileName == null || !fileName.toLowerCase().endsWith(".txt")) {
            request.setAttribute("message", "ERRORE: Sono ammessi solo file .txt!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }

        String mimeType = filePart.getContentType();
        if (mimeType == null || !mimeType.equals("text/plain")) {
            request.setAttribute("message", "ERRORE: Il tipo di file non è valido (MIME type errato).");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }

        String fileContent;
        try (InputStream inputStream = filePart.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            fileContent = reader.lines().collect(Collectors.joining("\n"));
        }

        if (fileContent.trim().isEmpty()) {
            request.setAttribute("message", "ERRORE: Il file è vuoto.");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO uploads (user_id, filename, content) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, user.getId());
                stmt.setString(2, fileName);
                stmt.setString(3, fileContent);
                stmt.executeUpdate();
            }
            request.setAttribute("message", "Successo! File caricato e salvato nel database.");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Errore Database: " + e.getMessage());
        }

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
}