package it.uniba.controller;

import it.uniba.model.Post; // Importa la classe nuova
import it.uniba.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/feed")
public class FeedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Post> posts = new ArrayList<>();
        // Query per recuperare post e autore
        String sql = "SELECT u.email, up.filename, up.uploaded_at, up.content " +
                "FROM uploads up JOIN users u ON up.user_id = u.id " +
                "ORDER BY up.uploaded_at DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Post p = new Post();
                p.setAuthor(rs.getString("email"));
                p.setFilename(rs.getString("filename"));
                p.setDate(rs.getString("uploaded_at"));
                p.setContent(rs.getString("content"));
                posts.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }

        request.setAttribute("posts", posts);
        request.getRequestDispatcher("feed.jsp").forward(request, response);
    }
}