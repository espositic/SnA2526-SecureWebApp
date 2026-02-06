<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Area Riservata</title></head>
<body>
<%-- NON serve più il controllo <c:if test empty user> qui, ci pensa il Filter! --%>

<h1>Area Riservata</h1>
<p>Benvenuto, <b><c:out value="${sessionScope.user.email}"/></b>!</p>

<hr>
<%-- Tasto Logout --%>
<a href="logout">
    <button>
        Disconnetti
    </button>
</a>
</body>
</html>