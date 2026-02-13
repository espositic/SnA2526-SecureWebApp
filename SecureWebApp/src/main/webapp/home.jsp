<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="header">
    <div class="header-inner">
        <div class="brand">
            <img src="img/logo.png" alt="Logo">
            <span>Sicurezza nelle Applicazioni</span>
        </div>
        <div class="nav">
            <a href="logout">Esci</a>
        </div>
    </div>
</div>

<main>
    <div class="card">
        <h3>Crea Post</h3>
        <p>Ciao <b><c:out value="${sessionScope.user.email}"/></b>, condividi un file di testo.</p>

        <% String msg = (String) request.getAttribute("message"); %>
        <% if (msg != null) { %>
        <div class="<%= msg.startsWith("Successo") ? "alert alert-success" : "alert alert-error" %>">
            <%= msg %>
        </div>
        <% } %>

        <form action="upload" method="post" enctype="multipart/form-data">
            <div class="form-row" style="display:flex; gap:10px; align-items:center;">
                <input type="file" name="file" accept=".txt" required style="background:#fff;">
            </div>
            <button type="submit">Pubblica</button>
        </form>
    </div>

    <div class="card" style="display: flex; justify-content: space-between; align-items: center;">
        <div>
            <h3 style="border:none; margin:0;">Bacheca</h3>
            <p style="margin:0;">Visualizza i file degli altri utenti.</p>
        </div>
        <div style="width: auto;">
            <a href="feed"><button style="width: auto; padding: 10px 20px;">Vai alla Bacheca &rarr;</button></a>
        </div>
    </div>
</main>

<div class="footer">
    CdL Magistrale in Sicurezza Informatica – UniBa<br>
    Studente: <strong>Matteo Esposito</strong>
</div>
</body>
</html>