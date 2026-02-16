<%--
  Pagina: error.jsp
  Descrizione: Pagina di errore generica dell'applicazione.

  Note di sicurezza:
  - Information Leakage: Non stampa mai exception.getMessage() o lo stack trace
    per evitare di fornire dettagli sull'infrastruttura a potenziali attaccanti.
  - User Experience: Offre una via d'uscita pulita verso la home.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Errore di Sistema - Secure App</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="header" style="background:#d93025;">
    <div class="header-inner">
        <div class="brand">
            <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo">
            <span>Errore di Sistema</span>
        </div>
    </div>
</div>

<main>
    <div class="card card-center">
        <h2 style="color:#d93025;">Si è verificato un problema</h2>

        <p>L'operazione richiesta non è stata completata a causa di un errore tecnico interno.</p>

        <%-- Recuperiamo il codice di stato HTTP per una comunicazione minima ma utile --%>
        <div style="background: #fdf2f2; border: 1px solid #f8b4b4; padding: 10px; border-radius: 4px; color: #9b1c1c; font-family: monospace; font-size: 0.9em; margin: 15px 0;">
            Codice Errore:
            <c:choose>
                <c:when test="${not empty pageContext.errorData}">
                    <c:out value="${pageContext.errorData.statusCode}"/>
                </c:when>
                <c:otherwise>500</c:otherwise>
            </c:choose>
        </div>

        <p>Ti preghiamo di riprovare più tardi o di contattare l'amministratore.</p>

        <hr>

        <div style="text-align: center;">
            <a href="${pageContext.request.contextPath}/home.jsp">
                <button type="button">Torna alla Home</button>
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