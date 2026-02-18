package it.uniba.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Classe che rappresenta l'entità di Upload.
 * Mappa la struttura della tabella 'uploads' del database e funge da ponte
 * tra il livello dati e la vista.
 *
 * <p>Oltre ai campi mappati sul DB, questa classe include proprietà aggiuntive
 * (come l'autore e il contenuto) popolate a runtime per arricchire la visualizzazione.</p>
 *
 * <p>Implementa {@link Serializable} per permettere al server di gestire
 * la sessione e il trasferimento dati.</p>
 *
 * @author Matteo Esposito
 * @version 1.0
 */
public class Upload implements Serializable {

    /**
     * Identificativo univoco per la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identificativo univoco dell'upload (Chiave Primaria).
     */
    private int id;

    /**
     * Identificativo dell'utente proprietario (Chiave Esterna su 'users').
     */
    private int userId;

    /**
     * Nome del file salvato fisicamente su disco.
     */
    private String filename;

    /**
     * Timestamp di creazione del record.
     */
    private Timestamp uploadedAt;

    /**
     * Email dell'autore.
     * <p>Questo campo non esiste nella tabella 'uploads' ma viene popolato
     * tramite JOIN sulla tabella 'users' per scopi di visualizzazione.</p>
     */
    private String author;

    /**
     * Contenuto testuale del file.
     * <p>Dato letto dal File System e non direttamente dal database.</p>
     */
    private String content;

    /**
     * Costruttore vuoto (No-Args).
     * Obbligatorio per le specifiche JavaBean e per framework di persistenza
     * o strumenti di serializzazione.
     */
    public Upload() {
    }

    /**
     * Restituisce l'ID dell'upload.
     *
     * @return L'intero rappresentante la chiave primaria.
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID dell'upload.
     *
     * @param id L'identificativo univoco.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'ID dell'utente proprietario.
     *
     * @return L'intero che rappresenta la chiave esterna dell'utente.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Imposta l'ID dell'utente proprietario.
     *
     * @param userId L'ID dell'utente da associare al file.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Restituisce il nome del file.
     *
     * @return La stringa contenente il nome del file su disco.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Imposta il nome del file.
     *
     * @param filename Il nome del file salvato.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Restituisce il timestamp di caricamento.
     *
     * @return L'oggetto Timestamp della creazione.
     */
    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    /**
     * Imposta il timestamp di caricamento.
     *
     * @param uploadedAt La data e ora del caricamento.
     */
    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    /**
     * Restituisce l'email dell'autore (campo calcolato).
     *
     * @return La stringa contenente l'email dell'autore.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Imposta l'email dell'autore.
     *
     * @param author L'email recuperata tramite join.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Restituisce il contenuto del file (campo calcolato).
     *
     * @return La stringa con il contenuto testuale.
     */
    public String getContent() {
        return content;
    }

    /**
     * Imposta il contenuto del file.
     *
     * @param content Il testo letto dal file.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Metodo di utilità per la compatibilità con le JSP.
     * <p>Alcune librerie di visualizzazione cercano automaticamente la proprietà "date"
     * invece di "uploadedAt". Questo alias facilita il binding dei dati nella vista.</p>
     *
     * @return Il timestamp di caricamento (alias di {@link #getUploadedAt()}).
     */
    public Timestamp getDate() {
        return uploadedAt;
    }
}