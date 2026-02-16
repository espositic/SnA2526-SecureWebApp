<%--
    Pagina JSP: feed.jsp (Bacheca)
    Descrizione: Visualizza i file caricati dagli utenti recuperati dalla FeedServlet.

    Note:
    - Utilizza JSTL (Core e Functions) per la logica e la sicurezza.
    - Applica l'escaping dei caratteri per prevenire attacchi Cross-Site Scripting (XSS).
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
    SECURITY CHECK (Client-Side/View-Side):
    Verifica se l'oggetto 'user' è presente nella sessione.
    Se l'utente accede direttamente alla JSP senza passare per la Servlet o se la sessione è scaduta,
    viene reindirizzato alla pagina di login.
--%>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp"/>
</c:if>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Feed</title>

    <%-- Collegamento al foglio di stile esterno --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

    <style>
        /**
         * STILE PER IL CONTENUTO DEI FILE:
         * white-space: pre-wrap garantisce che i ritorni a capo (\n) del file .txt
         * originale vengano mantenuti anche nella visualizzazione HTML.
         */
        .file-content p {
            white-space: pre-wrap;
            word-wrap: break-word;
            font-family: 'Courier New', Courier, monospace;
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            border: 1px solid #dee2e6;
            color: #333;
        }
    </style>
</head>
<body>

<%-- Header dell'applicazione: Branding e Navigazione --%>
<div class="header">
    <div class="header-inner">
        <div class="brand">
            <img src="img/logo.png" alt="Logo">
            <span>Sicurezza nelle Applicazioni</span>
        </div>
        <div class="nav">
            <%-- Link alla Home (upload) e Logout --%>
            <a href="home.jsp">Nuovo Post</a>
            <a href="logout">Esci</a>
        </div>
    </div>
</div>

<main>
    <%--
        LOGICA DI VISUALIZZAZIONE (JSTL):
        Controlla se la lista 'posts' (inviata dalla FeedServlet via request.setAttribute) è popolata.
    --%>
    <c:choose>
        <%-- CASO 1: Presenza di post nel sistema --%>
        <c:when test="${not empty posts}">
            <%-- Iterazione sulla lista degli oggetti Upload --%>
            <c:forEach var="p" items="${posts}">
                <div class="card">
                        <%-- Intestazione del Post: Autore, Data e Nome File --%>
                    <div style="display:flex; justify-content:space-between; margin-bottom:10px;">
                        <div>
                                <%-- fn:escapeXml previene attacchi XSS effettuando l'encoding dei caratteri speciali --%>
                            <strong><c:out value="${p.author}" /></strong>
                            <div style="font-size:12px; color: #666;">
                                <c:out value="${p.date}" />
                            </div>
                        </div>
                        <div style="font-size:12px; background:#e4e6eb; padding:2px 8px; border-radius:10px; height:fit-content;">
                            <c:out value="${p.filename}" />
                        </div>
                    </div>

                        <%-- Sezione Contenuto: Visualizza il testo letto dal file system --%>
                    <div class="file-content">
                            <%-- c:out applica automaticamente l'escaping (default escapeXml=true) --%>
                        <p><c:out value="${p.content}" /></p>
                    </div>
                </div>
            </c:forEach>
        </c:when>

        <%-- CASO 2: Nessun post trovato nel Database --%>
        <c:otherwise>
            <div class="card" style="text-align:center;">
                <p>La bacheca è attualmente vuota.</p>
                <a href="home.jsp" class="btn">Carica il primo pensiero</a>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<div class="footer">
    CdL Magistrale in Sicurezza Informatica – UniBa<br>
    Studente: <strong>Matteo Esposito</strong>
</div>

</body>
</html>