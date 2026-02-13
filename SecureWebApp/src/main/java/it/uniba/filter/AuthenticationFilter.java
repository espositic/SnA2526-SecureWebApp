package it.uniba.filter;

import it.uniba.util.CookieUtil;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + "/login";
        String registerURI = httpRequest.getContextPath() + "/register";
        String loginPage = httpRequest.getContextPath() + "/login.jsp";
        String registerPage = httpRequest.getContextPath() + "/register.jsp";

        boolean isStaticResource = httpRequest.getRequestURI().contains("/css/") || httpRequest.getRequestURI().endsWith(".css") ||
                httpRequest.getRequestURI().contains("/img/") || httpRequest.getRequestURI().endsWith(".png") || httpRequest.getRequestURI().endsWith(".jpg");

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        boolean isPublicPage = httpRequest.getRequestURI().equals(loginURI) ||
                httpRequest.getRequestURI().equals(loginPage) ||
                httpRequest.getRequestURI().equals(registerURI) ||
                httpRequest.getRequestURI().equals(registerPage);

        if (isLoggedIn || isPublicPage || isStaticResource) {

            if (isLoggedIn && !isStaticResource) {

                httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                httpResponse.setHeader("Pragma", "no-cache");
                httpResponse.setDateHeader("Expires", 0);

                String cookieValue = getCookieValue(httpRequest, CookieUtil.COOKIE_NAME);
                String sessionToken = (String) session.getAttribute("secureToken");

                System.out.println("DEBUG SECURITY:");
                System.out.println("1. Cookie Grezzo ricevuto: " + cookieValue);
                System.out.println("2. Token in Sessione: " + sessionToken);

                String verifiedTokenId = CookieUtil.verifyAndGetValue(cookieValue);
                System.out.println("3. Token Verificato dalla firma: " + verifiedTokenId);

                if (verifiedTokenId == null || sessionToken == null || !verifiedTokenId.equals(sessionToken)) {

                    System.err.println("Cookie manomesso o non valido.");

                    session.invalidate();
                    httpResponse.sendRedirect(loginPage + "?error=tampering");
                    return;
                }
            }

            chain.doFilter(request, response);

        } else {
            if (httpRequest.getRequestedSessionId() != null && !httpRequest.isRequestedSessionIdValid()) {
                httpResponse.sendRedirect(loginPage + "?error=expired");
            } else {
                httpResponse.sendRedirect(loginPage);
            }
        }
    }

    private String getCookieValue(HttpServletRequest req, String cookieName) {
        if (req.getCookies() == null) return null;
        for (Cookie c : req.getCookies()) {
            if (cookieName.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}