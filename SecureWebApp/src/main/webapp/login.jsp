<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Accedi</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="header">
    <div class="header-inner">
        <div class="brand">
            <img src="img/logo.png" alt="Logo">
            <span>Sicurezza nelle Applicazioni</span>
        </div>
    </div>
</div>

<main>
    <div class="card card-center">
        <h2>Accedi</h2>

        <c:if test="${param.registered eq 'true'}">
            <div class="alert alert-success">Registrazione completata! Effettua il login.</div>
        </c:if>
        <c:if test="${param.logout eq 'true'}">
            <div class="alert alert-success">Logout effettuato.</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}"/></div>
        </c:if>

        <form action="login" method="post">
            <div class="form-row">
                <input type="email" name="email" placeholder="Email" required>
            </div>
            <div class="form-row">
                <input type="password" name="password" placeholder="Password" required>
            </div>
            <button type="submit">Entra</button>
        </form>

        <hr>
        <a href="register.jsp"><button class="btn-secondary">Crea nuovo account</button></a>
    </div>
</main>

<div class="footer">
    CdL Magistrale in Sicurezza Informatica – UniBa<br>
    Studente: <strong>Matteo Esposito</strong>
</div>
</body>
</html>