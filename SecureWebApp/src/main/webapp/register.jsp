<%--
  Pagina: register.jsp
  Descrizione: Interfaccia utente per la creazione di un nuovo account.

  Note:
  - Sicurezza: Repopulation sicura con <c:out> per evitare Reflected XSS.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registrati - Secure App</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<%-- HEADER --%>
<div class="header">
    <div class="header-inner">
        <div class="brand">
            <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo">
            <span>Sicurezza nelle Applicazioni</span>
        </div>
        <div class="nav">
            <a href="${pageContext.request.contextPath}/login.jsp">Accedi</a>
        </div>
    </div>
</div>

<main>
    <div class="card card-center">
        <h2>Crea Account</h2>
        <hr>

        <%-- BLOCCO ERRORI --%>
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <c:out value="${error}"/>
            </div>
        </c:if>

        <%-- FORM DI REGISTRAZIONE --%>
        <form action="${pageContext.request.contextPath}/register" method="post" autocomplete="off">

            <div class="form-row">
                <input type="email"
                       name="email"
                       placeholder="Email"
                       value="<c:out value="${emailValue}"/>"
                       required>
            </div>

            <div class="form-row">
                <input type="password"
                       name="password"
                       placeholder="Nuova Password"
                       required>

                <div class="info-text">
                    Richiesto: Min 8 caratteri, 1 Maiuscola, 1 Numero, 1 Speciale.
                </div>
            </div>

            <div class="form-row">
                <input type="password" name="confirmPassword" placeholder="Conferma Password" required>
            </div>

            <button type="submit" class="btn-secondary">Iscriviti</button>
        </form>
    </div>
</main>

<%-- FOOTER --%>
<div class="footer">
    CdL Magistrale in Sicurezza Informatica – UniBa<br>
    Studente: <strong>Matteo Esposito</strong>
</div>

</body>
</html>