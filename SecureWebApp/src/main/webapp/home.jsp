<%--
  Pagina: home.jsp
  Descrizione: Area riservata post-login. Consente l'upload di file .txt.

  Note di sicurezza:
  - XSS: Utilizzo sistematico di <c:out> per visualizzare l'email utente e i messaggi.
  - Upload: Form configurato correttamente con enctype="multipart/form-data".
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Home - Secure App</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="header">
    <div class="header-inner">
        <div class="brand">
            <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo">
            <span>Sicurezza nelle Applicazioni</span>
        </div>
        <div class="nav">
            <a href="${pageContext.request.contextPath}/logout">Esci</a>
        </div>
    </div>
</div>

<main>
    <div class="card">
        <h3>Crea Post</h3>
        <%-- L'attributo 'user' in sessione contiene l'email (String) impostata dalla LoginServlet --%>
        <p>Benvenuto, <b><c:out value="${sessionScope.user}"/></b>. Condividi un file di testo.</p>

        <%--
           GESTIONE FEEDBACK (Messaggi di errore o successo)
           Controlliamo sia l'attributo della richiesta che quello della sessione (flash message)
        --%>
        <c:set var="displayMsg" value="${not empty message ? message : sessionScope.flashMessage}" />

        <c:if test="${not empty displayMsg}">
            <div class="alert ${fn:contains(displayMsg, 'Successo') ? 'alert-success' : 'alert-error'}">
                <c:out value="${displayMsg}"/>
            </div>
            <%-- Pulizia del messaggio dalla sessione dopo la visualizzazione --%>
            <c:remove var="flashMessage" scope="session" />
        </c:if>

        <%-- Form per l'upload del file --%>
        <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
            <div class="form-row" style="display:flex; gap:10px; align-items:center;">
                <input type="file" name="file" accept=".txt" required style="background:#fff; border: 1px solid #ccc; padding: 5px;">
            </div>
            <button type="submit">Pubblica</button>
        </form>
    </div>

    <div class="card" style="display: flex; justify-content: space-between; align-items: center;">
        <div>
            <h3 style="border:none; margin:0;">Bacheca</h3>
            <p style="margin:0;">Visualizza i file caricati dalla community.</p>
        </div>
        <div style="width: auto;">
            <%-- Link alla bacheca (Feed) --%>
            <a href="${pageContext.request.contextPath}/feed">
                <button type="button" style="width: auto; padding: 10px 20px;">Vai alla Bacheca &rarr;</button>
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