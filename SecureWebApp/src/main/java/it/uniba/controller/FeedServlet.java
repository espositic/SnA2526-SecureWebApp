package it.uniba.controller;

import it.uniba.model.Upload;
import it.uniba.util.DatabaseUtil;
import it.uniba.util.FileManagerUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet che gestisce la visualizzazione del feed.
 * Recupera i metadati degli upload dal database e, per ogni record,
 * legge il contenuto testuale del file corrispondente dal File System.
 *
 * @author Matteo Esposito
 * @version 1.0
 */
@WebServlet("/feed")
public class FeedServlet extends HttpServlet {

    /**
     * Gestisce la richiesta GET per visualizzare la bacheca.
     * Implementa il controllo di sessione e il recupero dei dati aggregati.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        List<Upload> posts = new ArrayList<>();

        // ========================================================================
        // QUERY SQL
        // ========================================================================
        // NOTA: Recuperiamo l'email dell'autore direttamente per popolare la vista.
        String sql = "SELECT u.email, up.filename, up.uploaded_at " +
                "FROM uploads up " +
                "JOIN users u ON up.user_id = u.id " +
                "ORDER BY up.uploaded_at DESC";

        // Recuperiamo il path assoluto della cartella 'uploads' sul server.
        String uploadDir = getServletContext().getRealPath("") + File.separator + "uploads";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Upload p = new Upload();
                // Mapping metadati dal database
                p.setFilename(rs.getString("filename"));
                p.setUploadedAt(rs.getTimestamp("uploaded_at"));
                // Popoliamo il campo email recuperato dalla JOIN
                p.setAuthor(rs.getString("email"));

                // ========================================================================
                // LETTURA DAL DISCO
                // ========================================================================
                try {
                    String realContent = FileManagerUtil.readFile(uploadDir, p.getFilename());
                    p.setContent(realContent);
                } catch (IOException e) {
                    // Se il file è rimosso o illeggibile, non bloccare il feed
                    p.setContent("Attenzione: Impossibile recuperare il contenuto di questo file.");
                }
                posts.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("posts", posts);
        request.getRequestDispatcher("feed.jsp").forward(request, response);
    }
}