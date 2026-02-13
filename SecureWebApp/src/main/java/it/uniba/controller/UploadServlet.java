package it.uniba.controller;

import it.uniba.model.User;
import it.uniba.util.DatabaseUtil;
import it.uniba.service.AsyncFileArchiver;
import org.apache.tika.Tika;

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

        System.out.println("DEBUG: Sto per inizializzare Apache Tika..."); // CHECKPOINT 1

        String detectedType = "unknown";
        try {
            // Usiamo Tika per rilevare il VERO tipo del file
            Tika tika = new Tika();

            try (InputStream checkStream = filePart.getInputStream()) {
                detectedType = tika.detect(checkStream);
            }
            System.out.println("DEBUG: Tika ha rilevato: " + detectedType); // CHECKPOINT 2

        } catch (Throwable t) {
            // CATTURA TUTTO: Anche errori di link o librerie mancanti
            System.err.println("!!! ERRORE CRITICO TIKA !!!");
            t.printStackTrace(); // Questo DEVE apparire nei log

            request.setAttribute("message", "ERRORE INTERNO: Impossibile analizzare il file. Contattare l'admin.");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }
        // Validazione rigorosa
        if (!"text/plain".equals(detectedType)) {
            System.err.println("SECURITY: Tentativo di upload file non valido. Rilevato: " + detectedType);
            request.setAttribute("message", "ERRORE: Formato file non supportato (Rilevato contenuto non testuale: " + detectedType + ").");
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

                AsyncFileArchiver.getInstance().archiveFileAsync(
                        fileName,
                        fileContent,
                        user.getEmail()
                );
            }
            request.setAttribute("message", "Successo! File caricato e salvato nel database.");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Errore Database: " + e.getMessage());
        }

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
}