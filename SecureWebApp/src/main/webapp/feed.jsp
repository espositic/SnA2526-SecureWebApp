<%--
  PAGINA: feed.jsp
  DESCRIZIONE: Visualizza la bacheca con i file di testo caricati dagli utenti.

  NOTE DI SICUREZZA & IMPLEMENTATIVE:
  1. Il contenuto dei file viene renderizzato tramite <c:out>.
  2. La pagina riceve una lista di oggetti pronta dalla FeedServlet.
  3. Verifica difensiva della sessione attiva.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
    CONTROLLO SESSIONE:
    Verifica ridondante: se la sessione è scaduta o l'accesso è diretto,
    si rimanda al login.
--%>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp"/>
</c:if>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Feed - Bacheca</title>

    <%-- Collegamento al foglio di stile --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

    <style>
        /**
         * VISUALIZZAZIONE FILE DI TESTO
         * white-space: pre-wrap è essenziale per rispettare la formattazione originale
         */
        .file-content p {
            white-space: pre-wrap;
            word-wrap: break-word;
            font-family: 'Courier New', Courier, monospace; /* Font monospaziato per simulare editor */
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            border: 1px solid #dee2e6;
            color: #333;
        }
    </style>
</head>
<body>

<%-- HEADER --%>
<div class="header">
    <div class="header-inner">
        <div class="brand">
            <img src="img/logo.png" alt="Logo">
            <span>Sicurezza nelle Applicazioni</span>
        </div>
        <div class="nav">
            <a href="home.jsp">Nuovo Post</a>
            <a href="logout">Esci</a>
        </div>
    </div>
</div>

<main>
    <%--
        LOGICA DI PRESENTAZIONE
        Itera sulla lista 'posts' preparata da FeedServlet.
    --%>
    <c:choose>
        <%-- CASO 1: Ci sono post da visualizzare --%>
        <c:when test="${not empty posts}">
            <c:forEach var="p" items="${posts}">
                <div class="card">

                        <%-- METADATI DEL POST --%>
                    <div style="display:flex; justify-content:space-between; margin-bottom:10px;">
                        <div>
                                <%--
                                    OUTPUT ENCODING
                                    Fondamentale se lo username permette caratteri speciali.
                                --%>
                            <strong><c:out value="${p.author}" /></strong>
                            <div style="font-size:12px; color: #666;">
                                <c:out value="${p.date}" />
                            </div>
                        </div>
                            <%-- Nome del file originale --%>
                        <div style="font-size:12px; background:#e4e6eb; padding:2px 8px; border-radius:10px; height:fit-content;">
                            <c:out value="${p.filename}" />
                        </div>
                    </div>

                        <%--
                            CONTENUTO DEL FILE
                            Qui viene stampato il contenuto letto dal disco con 'uso di <c:out>.
                        --%>
                    <div class="file-content">
                        <p><c:out value="${p.content}" /></p>
                    </div>
                </div>
            </c:forEach>
        </c:when>

        <%-- CASO 2: Nessun post trovato --%>
        <c:otherwise>
            <div class="card" style="text-align:center;">
                <p>La bacheca è attualmente vuota.</p>
                <a href="home.jsp" class="btn">Carica il primo post!</a>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<%-- FOOTER --%>
<div class="footer">
    CdL Magistrale in Sicurezza Informatica – UniBa<br>
    Studente: <strong>Matteo Esposito</strong>
</div>

</body>
</html>