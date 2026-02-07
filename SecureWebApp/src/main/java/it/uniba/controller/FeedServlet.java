package it.uniba.controller;

import it.uniba.model.Post;
import it.uniba.util.DatabaseUtil;
import it.uniba.util.SecurityUtil;

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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/feed")
public class FeedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'none'; object-src 'none'; style-src 'self' 'unsafe-inline';");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-XSS-Protection", "1; mode=block");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Post> posts = new ArrayList<>();
        String sql = "SELECT u.email, f.filename, f.content, f.uploaded_at " +
                "FROM uploads f JOIN users u ON f.user_id = u.id ORDER BY f.uploaded_at DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String rawContent = rs.getString("content");

                String safeContent = SecurityUtil.escapeHtml(rawContent);

                String safeAuthor = SecurityUtil.escapeHtml(rs.getString("email"));
                String safeFilename = SecurityUtil.escapeHtml(rs.getString("filename"));

                posts.add(new Post(safeAuthor, safeFilename, safeContent, rs.getString("uploaded_at")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("posts", posts);
        request.getRequestDispatcher("feed.jsp").forward(request, response);
    }
}