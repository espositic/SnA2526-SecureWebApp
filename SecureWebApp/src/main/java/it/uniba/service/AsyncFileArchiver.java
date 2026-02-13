package it.uniba.service;

import net.jcip.annotations.ThreadSafe;
import net.jcip.annotations.GuardedBy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gestore thread-safe per l'archiviazione concorrente dei file.
 * Implementa RF 3.8 usando Programmazione Difensiva.
 */
@ThreadSafe
public class AsyncFileArchiver {

    private static final AsyncFileArchiver INSTANCE = new AsyncFileArchiver();

    // Pool di thread per eseguire l'I/O senza bloccare la Servlet
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    // 1. STRATEGIA ATOMICITÀ: Contatore atomico per garantire nomi univoci senza lock complessi
    private final AtomicInteger fileCounter = new AtomicInteger(0);

    // 2. STRATEGIA LOCKING: Oggetto lock privato per proteggere la creazione della directory
    private final Object directoryLock = new Object();

    // Directory sicura (fuori dalla web root)
    private static final String ARCHIVE_DIR = System.getProperty("user.home") + File.separator + "SecureApp_Archive";

    private AsyncFileArchiver() {
        // Inizializzazione sicura
    }

    public static AsyncFileArchiver getInstance() {
        return INSTANCE;
    }

    /**
     * Metodo pubblico non bloccante.
     */
    public void archiveFileAsync(String originalFileName, String content, String uploaderEmail) {
        executor.submit(() -> safeWriteFile(originalFileName, content, uploaderEmail));
    }

    /**
     * Esegue la scrittura gestendo Race Conditions e TOCTOU.
     */
    private void safeWriteFile(String originalFileName, String content, String email) {

        // A. Generazione Nome Univoco (Thread-Safe via AtomicInteger)
        // Non serve synchronized qui perché incrementAndGet è atomico a livello hardware/CPU.
        int uniqueId = fileCounter.incrementAndGet();

        // Sanitizzazione base del nome file
        String safeName = "upload_" + uniqueId + "_" + originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        File destFile = new File(ARCHIVE_DIR, safeName);

        // B. Sezione Critica: Creazione Directory (TOCTOU Protection)
        // Usiamo un blocco sincronizzato limitato solo alla risorsa condivisa (la cartella)
        synchronized (directoryLock) {
            File dir = new File(ARCHIVE_DIR);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    System.err.println("ERRORE CRITICO: Impossibile creare directory di archiviazione.");
                    return;
                }
            }
        }

        // C. Scrittura File (Concorrente)
        // Poiché il nome è garantito univoco dall'AtomicInteger, possiamo scrivere
        // fuori dal blocco synchronized, massimizzando il parallelismo.
        try (FileOutputStream fos = new FileOutputStream(destFile, true)) { // true = append (opzionale)

            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String logEntry = String.format("METADATA [User: %s | Date: %s]\n%s\n------------------\n",
                    email, timestamp, content);

            fos.write(logEntry.getBytes(StandardCharsets.UTF_8));
            System.out.println("ARCHIVIO: File salvato in modo sicuro: " + destFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}