package it.uniba.model;

import java.io.Serializable;

/**
 * Classe che rappresenta l'entità Utente nel sistema.
 * Mappa la struttura della tabella 'users' del database.
 *
 * <p>Implementa {@link Serializable} per permettere al server di gestire
 * la sessione.</p>
 *
 * @author Matteo Esposito
 * @version 1.0
 */
public class User implements Serializable {

    /**
     * Identificativo univoco per la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identificativo univoco dell'utente (Chiave Primaria).
     */
    private int id;

    /**
     * Indirizzo email dell'utente, utilizzato come username per il login.
     */
    private String email;

    /**
     * Hash della password.
     */
    private String passwordHash;

    /**
     * Costruttore vuoto (No-Args).
     * Obbligatorio per le specifiche JavaBean e per framework come Hibernate o
     * strumenti di serializzazione JSON/XML.
     */
    public User() {
    }

    /**
     * Costruttore per creare un nuovo oggetto utente
     *
     * @param email L'email dell'utente.
     * @param passwordHash L'hash della password già calcolato.
     */
    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /**
     * Restituisce l'ID dell'utente.
     *
     * @return L'intero rappresentante la chiave primaria.
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID dell'utente.
     *
     * @param id L'identificativo univoco.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'email dell'utente.
     *
     * @return La stringa contenente l'email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email dell'utente.
     *
     * @param email L'indirizzo email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce l'hash della password.
     *
     * @return La stringa contenente l'hash cifrato.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Imposta l'hash della password.
     *
     * @param passwordHash L'hash della password da memorizzare.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Rappresentazione in stringa dell'oggetto Utente.
     * Utile per il logging e il debugging.
     *
     * <p><strong>SICUREZZA:</strong> Questo metodo è stato sovrascritto intenzionalmente
     * per NON includere il campo {@code passwordHash}, evitando che credenziali
     * (anche se hashate) finiscano nei log in chiaro.</p>
     *
     * @return Una stringa contenente i dati pubblici dell'utente.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}