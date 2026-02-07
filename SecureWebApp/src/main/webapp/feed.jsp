<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="java.util.List" %>
<%@ page import="it.uniba.model.Post" %>
<%@ page import="it.uniba.model.User" %>

<%
    HttpSession currentSession = request.getSession(false);
    if (currentSession == null || currentSession.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    User user = (User) currentSession.getAttribute("user");
    List<Post> posts = (List<Post>) request.getAttribute("posts");
%>

<html>
<head>
    <title>Bacheca Condivisa</title>
</head>
<body>
<h2>Bacheca Sicura</h2>
<p>Benvenuto, <b><%= user.getEmail() %></b>. <a href="home.jsp">Torna alla Home</a> | <a href="logout">Logout</a></p>
<hr>

<% if (posts != null) {
    for (Post p : posts) { %>
<div class="post">
    <p><strong>Utente:</strong> <%= p.getEmail() %> | <strong>File:</strong> <%= p.getFilename() %></p>

    <div class="content"><%= p.getContent() %></div>

    <small>Caricato il: <%= p.getDate() %></small>
</div>
<%  }
} else { %>
<p>Nessun post da mostrare.</p>
<% } %>
</body>
</html>