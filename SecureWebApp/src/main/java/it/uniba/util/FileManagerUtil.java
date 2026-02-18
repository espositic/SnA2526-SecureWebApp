package it.uniba.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Classe per la gestione del File System.
 * Gestisce la scrittura e la lettura dei file in modo thread-safe,
 * prevenendo conflitti di nomi e problemi di codifica.
 *
 * @author Matteo Esposito
 * @version 1.0
 */
public class FileManagerUtil {

    /**
     * Oggetto monitor condiviso per la sincronizzazione dei thread.
     * Utilizzato per creare una sezione critica durante il controllo
     * dell'esistenza del file e la sua creazione.
     */
    private static final Object LOCK = new Object();

    /**
     * Costruttore privato per impedire l'istanziazione.
     */
    private FileManagerUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Salva un file in una directory specifica gestendo la concorrenza.
     * Se un file con lo stesso nome esiste già, viene generato un nuovo nome
     * univoco incrementale (es. file.txt -> file_1.txt).
     *
     * @param directoryPath Il percorso della cartella di destinazione.
     * @param originalName Il nome originale del file.
     * @param content Il contenuto testuale da scrivere nel file.
     * @return Il nome definitivo del file salvato.
     * @throws IOException Se si verificano errori di I/O.
     */
    public static String saveFileSafe(String directoryPath, String originalName, String content) throws IOException {

        File dir = new File(directoryPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created && !dir.exists()) {
                throw new IOException("Impossibile creare la directory: " + directoryPath);
            }
        }

        // Controllo di sicurezza base per evitare Path Traversal
        if (originalName.contains("..") || originalName.contains("/") || originalName.contains("\\")) {
            throw new IllegalArgumentException("Il nome del file contiene caratteri non validi.");
        }

        /*
         * Blocco Synchronized per risolvere la Race Condition di tipo TOCTOU.
         */
        synchronized (LOCK) {
            String finalName = originalName;
            File destination = new File(dir, finalName);
            int i = 1;

            // Logica per gestire file con o senza estensione
            String nameWithoutExt;
            String ext;
            int dotIndex = originalName.lastIndexOf('.');

            if (dotIndex > 0) {
                nameWithoutExt = originalName.substring(0, dotIndex);
                ext = originalName.substring(dotIndex); // include il punto (es. ".txt")
            } else {
                nameWithoutExt = originalName;
                ext = "";
            }

            // Loop di controllo: finché esiste, incremento il numero.
            while (destination.exists()) {
                finalName = nameWithoutExt + "_" + i + ext;
                destination = new File(dir, finalName);
                i++;
            }

            // Scrittura effettiva usando UTF-8
            try (FileOutputStream fos = new FileOutputStream(destination);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {

                writer.write(content);
            }

            return finalName;
        }
    }

    /**
     * Legge il contenuto testuale di un file.
     *
     * @param directoryPath La cartella dove sono confinati i file.
     * @param filename Il nome del file richiesto.
     * @return Il contenuto del file come Stringa UTF-8.
     * @throws IOException Se si verificano errori di I/O o violazioni di sicurezza.
     */
    public static String readFile(String directoryPath, String filename) throws IOException {
        // Controllo Path Traversal anche in lettura
        File f = new File(directoryPath, filename);

        // Controlla che il file sia davvero dentro la directoryPath
        if (!f.getCanonicalPath().startsWith(new File(directoryPath).getCanonicalPath())) {
            throw new SecurityException("Tentativo di accesso non autorizzato al file system.");
        }

        if (!f.exists()) {
            return "File non trovato.";
        }

        // Legge i byte e li converte in stringa forzando UTF-8
        return new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
    }
}