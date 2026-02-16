package it.uniba.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Classe Model che rappresenta un'entità di Upload.
 * Questa classe funge da ponte tra il database e la vista.
 *
 * @author Matteo Esposito
 * @version 1.0
 */
public class Upload implements Serializable {

    // ========================================================================
    // CAMPI DI MAPPING
    // ========================================================================

    /** ID univoco dell'upload. */
    private int id;

    /** ID dell'utente proprietario (Foreign Key su 'users'). */
    private int userId;

    /** Nome del file salvato fisicamente su disco. */
    private String filename;

    /** Timestamp di creazione del record. */
    private Timestamp uploadedAt;

    // ========================================================================
    // CAMPI EXTRA
    // ========================================================================
    // Questi campi non esistono nella tabella 'uploads' ma vengono popolati
    // dalla Servlet per arricchire la vista.

    /** Email dell'autore (recuperata tramite JOIN sulla tabella 'users'). */
    private String author;

    /** Contenuto testuale del file (letto dal File System). */
    private String content;

    /**
     * Costruttore vuoto (JavaBean standard).
     */
    public Upload() {}

    // ========================================================================
    // GETTER E SETTER STANDARD
    // ========================================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public Timestamp getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Timestamp uploadedAt) { this.uploadedAt = uploadedAt; }

    // ========================================================================
    // GETTER E SETTER CAMPI EXTRA
    // ========================================================================

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    /**
     * Metodo di utilità per la JSP.
     * Spesso le librerie di visualizzazione cercano la proprietà "date"
     * invece di "uploadedAt". Questo alias facilita il binding.
     *
     * @return Il timestamp di caricamento.
     */
    public Timestamp getDate() { return uploadedAt; }
}