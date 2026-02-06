<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Registrazione Sicura</title>
</head>
<body>

<div class="container">
    <h2>Crea Account</h2>

    <c:if test="${not empty error}">
        <div class="error">
            <c:out value="${error}"/>
        </div>
    </c:if>

    <form action="register" method="post">
        <div>
            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email" required autocomplete="email">
        </div>

        <div>
            <label for="password">Password:</label><br>
            <input type="password" id="password" name="password" required autocomplete="new-password">
            <div class="info"><small>Min 8 caratteri, 1 Maiuscola, 1 Numero, 1 Speciale.</small></div>
        </div>

        <div>
            <label for="confirmPassword">Conferma Password:</label><br>
            <input type="password" id="confirmPassword" name="confirmPassword" required autocomplete="new-password">
        </div>

        <br>
        <button type="submit">Registrati</button>
    </form>
    
    <p>
        Hai già un account? <a href="login.jsp">Accedi</a>
    </p>
</div>

</body>
</html>