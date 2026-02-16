package it.uniba.controller;

import it.uniba.util.FileManagerUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Servlet di test per la verifica della concorrenza.
 * Simula il caricamento simultaneo di file con lo stesso nome da parte di più thread
 * per verificare la robustezza del meccanismo synchronized
 * implementato in FileManagerUtil.
 *
 * @author Matteo Esposito
 * @version 1.0
 */
@WebServlet("/test-concurrency")
public class ConcurrencyTestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Esegue il test di concorrenza.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        // Configurazione del test
        int numberOfThreads = 20;
        String fileName = "test_file.txt";

        // Utilizza una cartella separata per non intasare quella principale
        String uploadPath = getServletContext().getRealPath("") + File.separator + "test_uploads";
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        } else {
            // Pulizia file precedenti
            File[] existingFiles = uploadDir.listFiles();
            if (existingFiles != null) {
                for (File f : existingFiles) {
                    if (f.isFile()) {
                        f.delete();
                    }
                }
            }
        }

        // Output Header
        out.println("<html><head><title>Concurrency Test</title></head><body>");
        out.println("<h1>Test Concorrenza File System</h1>");
        out.println("<p>Simulazione avviata: " + numberOfThreads + " thread tenteranno di scrivere '" + fileName + "' simultaneamente.</p>");
        out.println("<hr>");

        // Esecuzione Thread
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i + 1;

            executor.submit(() -> {
                try {
                    String content = "Contenuto scritto dal thread numero: " + threadId;
                    // Chiamata al metodo thread-safe
                    FileManagerUtil.saveFileSafe(uploadPath, fileName, content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Attesa completamento
        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (finished) {
                out.println("<p>Esecuzione thread completata.</p>");
            } else {
                out.println("<p>Attenzione: Timeout durante l'attesa dei thread.</p>");
            }
        } catch (InterruptedException e) {
            out.println("<p>Errore: Thread interrotto.</p>");
            Thread.currentThread().interrupt();
        }

        // Verifica Risultati
        File[] resultFiles = uploadDir.listFiles();
        int fileCount = (resultFiles != null) ? resultFiles.length : 0;

        out.println("<h3>Risultati:</h3>");
        out.println("<ul>");
        out.println("<li>Richieste inviate: " + numberOfThreads + "</li>");
        out.println("<li>File creati su disco: " + fileCount + "</li>");
        out.println("</ul>");

        if (fileCount == numberOfThreads) {
            out.println("<h2 style='color:green;'>TEST SUPERATO</h2>");
            out.println("<p>Tutti i file sono stati rinominati correttamente senza sovrascritture.</p>");
        } else {
            out.println("<h2 style='color:red;'>TEST FALLITO</h2>");
            out.println("<p>Rilevata Race Condition: alcuni file sono stati sovrascritti.</p>");
        }

        // Lista dei file generati per verifica visiva
        out.println("<hr><h4>Lista file generati:</h4><ul>");
        if (resultFiles != null) {
            // Ordinamento alfabetico semplice per visualizzare la sequenza
            java.util.Arrays.sort(resultFiles);
            for (File f : resultFiles) {
                if (f.isFile()) {
                    out.println("<li>" + f.getName() + "</li>");
                }
            }
        }
        out.println("</ul>");
        out.println("</body></html>");
    }
}