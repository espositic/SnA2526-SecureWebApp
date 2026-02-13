<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="java.util.List" %>
<%@ page import="it.uniba.model.Post" %>
<%
    HttpSession currentSession = request.getSession(false);
    if (currentSession == null || currentSession.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<Post> posts = (List<Post>) request.getAttribute("posts");
%>
<html>
<head>
    <title>Bacheca</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="header">
    <div class="header-inner">
        <div class="brand">
            <img src="img/logo.png" alt="Logo">
            <span>Bacheca</span>
        </div>
        <div class="nav">
            <a href="home.jsp">Home</a>
            <a href="logout">Esci</a>
        </div>
    </div>
</div>

<main>
    <% if (posts != null && !posts.isEmpty()) {
        for (Post p : posts) { %>
    <div class="card">
        <div style="display:flex; justify-content:space-between; margin-bottom:10px;">
            <div>
                <strong><%= p.getAuthor() %></strong>
                <div style="font-size:12px; color:var(--muted);"><%= p.getDate() %></div>
            </div>
            <div style="font-size:12px; background:#e4e6eb; padding:2px 8px; border-radius:10px; height:fit-content;">
                <%= p.getFilename() %>
            </div>
        </div>

        <div class="file-content"><%= p.getContent() %></div>

    </div>
    <% }
    } else { %>
    <div class="card" style="text-align:center;">
        <p>Nessun post presente.</p>
        <a href="home.jsp">Pubbilcalo per primo!</a>
    </div>
    <% } %>
</main>

<div class="footer">
    CdL Magistrale in Sicurezza Informatica – UniBa<br>
    <strong>Matteo Esposito</strong>
</div>
</body>
</html>