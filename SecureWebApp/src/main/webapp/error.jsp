<%--
  PAGINA: error.jsp
  Pagina globale per la gestione degli errori non gestiti.

  NOTE DI SICUREZZA & IMPLEMENTATIVE:
  1. Questa pagina è progettata per NON mostrare mai dettagli tecnici dell'errore.
  2. Fornisce un feedback generico ma cortese e una via d'uscita per evitare che l'utente rimanga bloccato.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Errore</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%-- HEADER: Stile differenziato  --%>
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

        <%-- Messaggio generico --%>
        <p>L'operazione richiesta non è stata completata a causa di un errore tecnico interno.</p>

        <%--
            FEEDBACK MINIMO (HTTP Status Code)
            Visualizziamo solo il codice numerico.
            Questo è utile per il debugging ma innocuo dal punto di vista della sicurezza,
            poiché non rivela la struttura interna o la logica del codice Java.
        --%>
        <div style="background: #fdf2f2; border: 1px solid #f8b4b4; padding: 10px; border-radius: 4px; color: #9b1c1c; font-family: monospace; font-size: 0.9em; margin: 15px 0;">
            Codice Errore:
            <c:choose>
                <%-- Se Tomcat fornisce i dati dell'errore --%>
                <c:when test="${not empty pageContext.errorData}">
                    <c:out value="${pageContext.errorData.statusCode}"/>
                </c:when>
                <%-- Fallback generico --%>
                <c:otherwise>500</c:otherwise>
            </c:choose>
        </div>

        <p>Ti preghiamo di riprovare più tardi o di contattare l'amministratore.</p>

        <hr>

        <%-- VIA D'USCITA: Riporta l'utente in un'area sicura --%>
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