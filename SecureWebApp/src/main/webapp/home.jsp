<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Area Riservata</title></head>
<body>
<%-- NON serve più il controllo <c:if test empty user> qui, ci pensa il Filter! --%>

<h1>Area Riservata</h1>
<p>Benvenuto, <b><c:out value="${sessionScope.user.email}"/></b>!</p>

<hr>

<h3>Carica un file di testo</h3>

<% String msg = (String) request.getAttribute("message"); %>
<% if (msg != null) { %>
<div><%= msg %></div>
<% } %>

<form action="upload" method="post" enctype="multipart/form-data">
    <label for="fileUpload">Seleziona un file .txt:</label>
    <input type="file" id="fileUpload" name="file" accept=".txt" required>
    <button type="submit">Carica File</button>
</form>

<hr>
<%-- Tasto Logout --%>
<a href="logout">
    <button>
        Disconnetti
    </button>
</a>
</body>
</html>