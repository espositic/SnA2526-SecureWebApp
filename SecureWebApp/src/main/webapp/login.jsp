<%--
  Pagina: login.jsp
  Descrizione: Pagina di autenticazione utente.

  Note di sicurezza:
  - XSS: Protezione tramite JSTL c:out per la stampa dell'errore.
  - Feedback: Gestione messaggi di successo/logout via parametri URL.
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

        <%-- Messaggio dopo registrazione riuscita --%>
        <c:if test="${param.registered eq 'true'}">
            <div class="alert alert-success">Registrazione completata! Effettua il login.</div>
        </c:if>

        <%-- Messaggio dopo logout --%>
        <c:if test="${param.logout eq 'true'}">
            <div class="alert alert-success">Logout effettuato con successo.</div>
        </c:if>

        <%-- Messaggio di errore generico (es. credenziali errate) --%>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                <c:out value="${errorMessage}"/>
            </div>
        </c:if>

        <%-- Form di Login: la validazione regex è delegata al backend --%>
        <form action="${pageContext.request.contextPath}/login" method="post" autocomplete="off">
            <div class="form-row">
                <input type="email" name="email" placeholder="Email" required>
            </div>
            <div class="form-row">
                <input type="password" name="password" placeholder="Password" required>
            </div>

            <%-- AGGIUNTA CHECKBOX RICORDAMI --%>
            <div class="form-row" style="display: flex; align-items: center; gap: 10px; margin-bottom: 15px;">
                <input type="checkbox" name="rememberMe" id="rememberMe" style="width: auto;">
                <label for="rememberMe" style="margin-bottom: 0; cursor: pointer; font-weight: normal; color: #1d1f23;">Ricordami</label>
            </div>
            <%-- FINE AGGIUNTA --%>

            <button type="submit">Entra</button>
        </form>

        <hr>
        <%-- Link alla registrazione --%>
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