<%--
  Pagina: home.jsp
  Descrizione: Dashboard utente per l'upload. Linka alla pagina Feed separata.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Controllo Sessione --%>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp"/>
</c:if>

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
        <%-- Visualizza l'email dell'utente loggato --%>
        <p>Benvenuto, <b><c:out value="${sessionScope.user.email}"/></b>. Condividi un file di testo.</p>

        <%--
            GESTIONE MESSAGGI (Feedback)
            Legge il parametro 'msg' inviato dalla UploadServlet via Redirect.
            Se contiene "Errore" -> Rosso (.alert-error)
            Altrimenti -> Blu (.alert-success)
        --%>
        <c:if test="${not empty param.msg}">
            <div class="alert ${fn:containsIgnoreCase(param.msg, 'Errore') ? 'alert-error' : 'alert-success'}">
                <c:out value="${param.msg}"/>
            </div>
        </c:if>

        <%-- Form Upload --%>
        <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
            <div class="form-row" style="display:flex; gap:10px; align-items:center;">
                <input type="file" name="file" accept=".txt" required style="background:#fff; border: 1px solid #ccc; padding: 5px; width: 100%;">
            </div>
            <button type="submit" style="margin-top: 10px;">Pubblica</button>
        </form>
    </div>

    <div class="card" style="display: flex; justify-content: space-between; align-items: center;">
        <div>
            <h3 style="border:none; margin:0; padding:0;">Bacheca</h3>
            <p style="margin:0; font-size: 14px; color: #65676b;">Visualizza i file caricati dalla community.</p>
        </div>
        <div style="width: auto;">
            <%-- Link alla Servlet /feed (che poi mostra feed.jsp) --%>
            <a href="${pageContext.request.contextPath}/feed" style="text-decoration: none;">
                <button type="button" style="width: auto; padding: 10px 20px; background: #42b72a;">Vai alla Bacheca &rarr;</button>
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