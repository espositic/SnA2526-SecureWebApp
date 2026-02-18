<%--
  PAGINA: login.jsp
  DESCRIZIONE: Interfaccia di autenticazione utente.

  NOTE DI SICUREZZA & IMPLEMENTATIVE:
  1. Utilizzo di JSTL <c:out> per l'output di messaggi dinamici.
  2. Gestione sicura dei messaggi di stato tramite parametri.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Accedi</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="header">
    <div class="header-inner">
        <div class="brand">
            <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo">
            <span>Sicurezza nelle Applicazioni</span>
        </div>
    </div>
</div>

<main>
    <div class="card card-center">
        <h2>Accedi</h2>

        <%--
             FEEDBACK UTENTE: REGISTRAZIONE
             Messaggio informativo statico, attivato da parametro URL sicuro.
        --%>
        <c:if test="${param.registered eq 'true'}">
            <div class="alert alert-success">Registrazione completata! Effettua il login.</div>
        </c:if>

        <%--
             FEEDBACK UTENTE: LOGOUT
             Conferma visiva dell'avvenuta invalidazione della sessione.
        --%>
        <c:if test="${param.logout eq 'true'}">
            <div class="alert alert-success">Logout effettuato con successo.</div>
        </c:if>

        <%--
             GESTIONE ERRORI (Login Failed)
             Visualizza errori generati dal backend.
             SECURITY: <c:out> neutralizza potenziali payload XSS se il messaggio deriva da input utente.
        --%>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                <c:out value="${errorMessage}"/>
            </div>
        </c:if>

        <%--
           MESSAGGI GENERICI
           Visualizza messaggi passati via parametro 'msg'.
           Anche qui l'escaping è fondamentale per prevenire Reflected XSS.
        --%>
        <c:if test="${not empty param.msg}">
            <div class="alert alert-success">
                <c:out value="${param.msg}"/>
            </div>
        </c:if>

        <%--
            FORM DI LOGIN SICURO
            - method="post": Obbligatorio per non esporre password in URL/Query String.
            - autocomplete="off": Mitigazione base per evitare caching credenziali su PC condivisi.
            - required: Validazione client-side per UX.
        --%>
        <form action="${pageContext.request.contextPath}/login" method="post" autocomplete="off">
            <div class="form-row">
                <input type="email" name="email" placeholder="Email" required>
            </div>
            <div class="form-row">
                <input type="password" name="password" placeholder="Password" required>
            </div>

            <button type="submit">Entra</button>
        </form>

        <hr>
        <%-- NAVIGAZIONE: Link alla pagina di registrazione --%>
        <div style="text-align: center;">
            <p>Non hai un account?</p>
            <a href="${pageContext.request.contextPath}/register.jsp">
                <button type="button" class="btn-secondary">Crea nuovo account</button>
            </a>
        </div>
    </div>
</main>

<div class="footer">
    CdL Magistrale in Sicurezza Informatica – UniBa<br>
    Studente: <strong>Matteo Esposito</strong>
</div>

</body>
</html>