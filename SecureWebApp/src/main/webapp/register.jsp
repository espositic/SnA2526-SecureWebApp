<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registrati</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="header">
    <div class="header-inner">
        <div class="brand">
            <img src="img/logo.png" alt="Logo">
            <span>UniBa Security</span>
        </div>
        <div class="nav"><a href="login.jsp">Accedi</a></div>
    </div>
</div>

<main>
    <div class="card card-center">
        <h2>Crea Account</h2>
        <p>È veloce e semplice.</p>
        <hr>

        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}"/></div>
        </c:if>

        <form action="register" method="post">
            <div class="form-row">
                <input type="email" name="email" placeholder="Email istituzionale" required>
            </div>
            <div class="form-row">
                <input type="password" name="password" placeholder="Nuova Password" required>
                <div class="info-text">Min 8 caratteri, 1 Maiuscola, 1 Numero, 1 Speciale.</div>
            </div>
            <div class="form-row">
                <input type="password" name="confirmPassword" placeholder="Conferma Password" required>
            </div>
            <button type="submit" class="btn-secondary">Iscriviti</button>
        </form>
    </div>
</main>

<div class="footer">
    CdL Magistrale in Sicurezza Informatica – UniBa<br>
    Studente: <strong>TUO NOME COGNOME</strong>
</div>
</body>
</html>