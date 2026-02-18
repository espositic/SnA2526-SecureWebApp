package it.uniba.controller;

import it.uniba.dao.UploadDAO;
import it.uniba.model.User;
import it.uniba.util.FileManagerUtil;
import org.apache.tika.Tika;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Servlet per la gestione sicura del caricamento dei file.
 * Implementa controlli  su estensione e contenuto
 * prima di salvare il file su disco e registrare l'evento nel database.
 *
 * <p>Configurazione Multipart:
 * <ul>
 * <li>Soglia scrittura su disco: 1 MB</li>
 * <li>Dimensione massima file: 5 MB</li>
 * <li>Dimensione massima richiesta: 10 MB</li>
 * </ul>
 * </p>
 *
 * @author Matteo Esposito
 * @version 1.0
 */
@WebServlet("/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
public class UploadServlet extends HttpServlet {

    private final UploadDAO uploadDAO = new UploadDAO();

    /**
     * Gestisce la richiesta POST per l'upload di un file.
     * Esegue la validazione, la conversione e il salvataggio.
     *
     * @param request  La richiesta HTTP contenente il file multipart.
     * @param response La risposta HTTP per il redirect.
     * @throws ServletException In caso di errori servlet.
     * @throws IOException      In caso di errori I/O.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // ========================================================================
        // RECUPERO UTENTE
        // ========================================================================
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        try {
            // ====================================================================
            // RECUPERO FILE E VALIDAZIONE
            // ====================================================================
            Part filePart = request.getPart("file");

            if (filePart != null && filePart.getSize() > 0) {
                String submittedName = filePart.getSubmittedFileName();

                // Si accettano solo file che terminano esplicitamente con .txt
                if (submittedName == null || !submittedName.toLowerCase().endsWith(".txt")) {
                    throw new SecurityException("Estensione non valida. Solo .txt ammessi.");
                }

                // Tika analizza i magic numbers del file.
                try (InputStream is = filePart.getInputStream()) {
                    Tika tika = new Tika();
                    String mimeType = tika.detect(is);
                    if (!"text/plain".equals(mimeType)) {
                        throw new SecurityException("MIME Type non valido: " + mimeType);
                    }
                }

                // ====================================================================
                // CONVERSIONE STREAM -> STRINGA
                // ====================================================================
                // FileManagerUtil richiede il contenuto come stringa.
                // Leggiamo lo stream in memoria con charset UTF-8 per evitare corruzione caratteri.
                String fileContent;
                try (InputStream inputStream = filePart.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    fileContent = reader.lines().collect(Collectors.joining("\n"));
                }

                if (fileContent.trim().isEmpty()) {
                    throw new SecurityException("Il file è vuoto.");
                }

                // ====================================================================
                // SALVATAGGIO SU FILE SYSTEM
                // ====================================================================
                // Determina il percorso assoluto della cartella 'uploads' nel server/container.
                String uploadDir = getServletContext().getRealPath("") + File.separator + "uploads";
                // Invoca il metodo thread-safe dell'utility per salvare fisicamente il file.
                // Ritorna il nome del file effettivamente salvato.
                String savedFileName = FileManagerUtil.saveFileSafe(uploadDir, submittedName, fileContent);

                // ====================================================================
                // SALVATAGGIO METADATI SU DB
                // ====================================================================
                boolean dbSuccess = uploadDAO.saveUpload(user.getId(), savedFileName);

                if (dbSuccess) {
                    response.sendRedirect("home.jsp?msg=Post pubblicato con successo!");
                } else {
                    response.sendRedirect("home.jsp?msg=Errore nel salvataggio DB");
                }
            } else {
                response.sendRedirect("home.jsp?msg=Errore: File vuoto o mancante.");
            }
        } catch (SecurityException se) {
            response.sendRedirect("home.jsp?msg=Errore Sicurezza: " + se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home.jsp?msg=Errore Tecnico: " + e.getMessage());
        }
    }
}