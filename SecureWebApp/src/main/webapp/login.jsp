<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login Sicuro</title>
</head>
<body>

<div class="container">
    <h2>Accesso Utente</h2>

    <%-- Messaggio di successo (dopo registrazione) --%>
    <c:if test="${param.registered eq 'true'}">
        <div style="color: green;">Registrazione completata! Effettua il login.</div>
    </c:if>

    <%-- Messaggio di Errore --%>
    <c:if test="${not empty error}">
        <div style="color: red;">
            <c:out value="${error}"/>
        </div>
    </c:if>

    <form action="login" method="post">
        <div>
            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email" required autocomplete="email">
        </div>

        <div>
            <label for="password">Password:</label><br>
            <input type="password" id="password" name="password" required autocomplete="current-password">
        </div>

        <br>
        <button type="submit">Accedi</button>
    </form>

    <p>
        Non hai un account? <a href="register.jsp">Registrati qui</a>
    </p>
</div>

</body>
</html>