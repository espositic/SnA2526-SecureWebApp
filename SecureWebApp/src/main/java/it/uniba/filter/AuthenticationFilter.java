package it.uniba.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
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

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean isLoginPage = httpRequest.getRequestURI().equals(loginPage);
        boolean isRegisterRequest = httpRequest.getRequestURI().equals(registerURI);
        boolean isRegisterPage = httpRequest.getRequestURI().equals(registerPage);

        boolean isStaticResource = httpRequest.getRequestURI().contains("/css/") || httpRequest.getRequestURI().endsWith(".css");


        if (isLoggedIn || isLoginRequest || isLoginPage || isRegisterRequest || isRegisterPage || isStaticResource) {


            if (isLoggedIn && !isStaticResource) {
                httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                httpResponse.setHeader("Pragma", "no-cache");
                httpResponse.setDateHeader("Expires", 0);
            }

            chain.doFilter(request, response);

        } else {
            System.out.println("Tentativo di accesso non autorizzato a: " + httpRequest.getRequestURI());
            httpResponse.sendRedirect(loginPage);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}