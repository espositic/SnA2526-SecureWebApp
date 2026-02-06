<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Home Protetta</title></head>
<body>
<%-- Controllo Sessione: Se non c'è l'utente, via al login --%>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp"/>
</c:if>

<h1>Area Riservata</h1>
<p>Benvenuto, <b><c:out value="${sessionScope.user.email}"/></b>!</p>
<p>ID Sessione: <%= session.getId() %> (Nota come cambia a ogni nuovo login)</p>

<hr>
<%-- Logout (simulato per ora, cancella sessione lato client o riavvia browser) --%>
<p>Sei dentro una sessione HTTP sicura.</p>
</body>
</html>