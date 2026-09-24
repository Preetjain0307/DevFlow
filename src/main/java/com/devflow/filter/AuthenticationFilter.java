package com.devflow.filter;

import com.devflow.config.Constants;
import com.devflow.model.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/dashboard", "/dashboard/*", 
        "/projects", "/projects/*", 
        "/tasks", "/tasks/*", 
        "/task", "/task/*", 
        "/bugs", "/bugs/*",
        "/meetings", "/meetings/*", 
        "/documents", "/documents/*", 
        "/ideas", "/ideas/*", 
        "/github", "/github/*",
        "/admin", "/admin/*", 
        "/reports", "/reports/*", 
        "/profile", "/profile/*", 
        "/notifications", "/notifications/*",
        "/sprints", "/sprints/*", 
        "/milestones", "/milestones/*", 
        "/api/*"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;

        if (currentUser == null) {
            String requestURI = request.getRequestURI();
            if (requestURI.contains("/api/")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":false,\"error\":\"Authentication required\"}");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/login?redirect=" + java.net.URLEncoder.encode(requestURI, "UTF-8"));
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
