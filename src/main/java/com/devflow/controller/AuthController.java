package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.exception.ValidationException;
import com.devflow.model.User;
import com.devflow.service.UserService;
import com.devflow.service.impl.UserServiceImpl;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AuthController", urlPatterns = {"/login", "/register", "/logout", "/profile", "/change-password", "/demo-login"})
public class AuthController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/login":
                showLogin(request, response);
                break;
            case "/demo-login":
                handleDemoLogin(request, response);
                break;
            case "/register":
                showRegister(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/profile":
                showProfile(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/login");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/login":
                handleLogin(request, response);
                break;
            case "/demo-login":
                handleDemoLogin(request, response);
                break;
            case "/register":
                handleRegister(request, response);
                break;
            case "/profile":
                handleUpdateProfile(request, response);
                break;
            case "/change-password":
                handleChangePassword(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/login");
                break;
        }
    }

    private void showLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(Constants.SESSION_USER) != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    private void showRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("roles", userService.getAllRoles());
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");
        String ipAddress = request.getRemoteAddr();

        try {
            User user = userService.authenticate(username, password, ipAddress);
            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute(Constants.SESSION_USER, user);
                session.setAttribute(Constants.SESSION_USER_ID, user.getId());
                session.setAttribute(Constants.SESSION_USER_ROLE, user.getRoleName());
                session.setMaxInactiveInterval(30 * 60); // 30 minutes session timeout

                if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("/login")) {
                    response.sendRedirect(redirect);
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
                return;
            } else {
                request.setAttribute("error", "Invalid username or password.");
            }
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred during login.");
        }

        request.setAttribute("username", username);
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String designation = request.getParameter("designation");
        String bio = request.getParameter("bio");
        String roleIdStr = request.getParameter("roleId");
        String ipAddress = request.getRemoteAddr();

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.setAttribute("roles", userService.getAllRoles());
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password); // Will be hashed in service
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setDesignation(designation);
        user.setBio(bio);
        try {
            user.setRoleId(Integer.parseInt(roleIdStr));
        } catch (Exception e) {
            user.setRoleId(Constants.ROLE_ID_DEVELOPER);
        }

        try {
            boolean registered = userService.register(user, ipAddress);
            if (registered) {
                request.getSession(true).setAttribute(Constants.FLASH_SUCCESS, "Registration successful! You can now log in.");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("error", "Failed to complete registration: " + e.getMessage());
        }

        request.setAttribute("roles", userService.getAllRoles());
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        User freshUser = userService.getUserById(currentUser.getId());
        session.setAttribute(Constants.SESSION_USER, freshUser);
        request.setAttribute("user", freshUser);
        request.getRequestDispatcher("/WEB-INF/views/auth/profile.jsp").forward(request, response);
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String designation = request.getParameter("designation");
        String bio = request.getParameter("bio");

        currentUser.setFullName(fullName);
        currentUser.setPhone(phone);
        currentUser.setDesignation(designation);
        currentUser.setBio(bio);

        try {
            userService.updateProfile(currentUser);
            session.setAttribute(Constants.SESSION_USER, currentUser);
            request.setAttribute("success", "Profile updated successfully.");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }

        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/WEB-INF/views/auth/profile.jsp").forward(request, response);
    }

    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");

        if (!newPassword.equals(confirmNewPassword)) {
            request.setAttribute("passwordError", "New passwords do not match.");
            showProfile(request, response);
            return;
        }

        try {
            userService.changePassword(currentUser.getId(), currentPassword, newPassword);
            request.setAttribute("passwordSuccess", "Password changed successfully.");
        } catch (Exception e) {
            request.setAttribute("passwordError", e.getMessage());
        }

        showProfile(request, response);
    }

    private void handleDemoLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roleParam = request.getParameter("role");
        String usernameParam = request.getParameter("username");
        String redirect = request.getParameter("redirect");

        String targetUsername = "admin";
        if (usernameParam != null && !usernameParam.trim().isEmpty()) {
            targetUsername = usernameParam.trim();
        } else if (roleParam != null) {
            switch (roleParam.toLowerCase().trim()) {
                case "admin":
                    targetUsername = "admin";
                    break;
                case "pm":
                case "project_manager":
                    targetUsername = "pm_sarah";
                    break;
                case "dev":
                case "developer":
                    targetUsername = "dev_alex";
                    break;
                case "priya":
                case "frontend":
                    targetUsername = "dev_priya";
                    break;
                case "tester":
                case "qa":
                    targetUsername = "tester_mark";
                    break;
                case "faculty":
                case "mentor":
                    targetUsername = "faculty_dr_alan";
                    break;
                case "demo":
                case "super":
                default:
                    targetUsername = "demo";
                    break;
            }
        }

        try {
            User user = userService.getUserByUsername(targetUsername);
            if (user == null && "demo".equals(targetUsername)) {
                user = userService.getUserByUsername("admin");
            }
            if (user == null) {
                java.util.List<User> all = userService.getAllUsers();
                if (all != null && !all.isEmpty()) {
                    user = all.get(0);
                }
            }

            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute(Constants.SESSION_USER, user);
                session.setAttribute(Constants.SESSION_USER_ID, user.getId());
                session.setAttribute(Constants.SESSION_USER_ROLE, user.getRoleName());
                session.setMaxInactiveInterval(30 * 60);

                session.setAttribute(Constants.FLASH_SUCCESS, "⚡ Instant Demo Access: Switched to " + user.getFullName() + " (" + user.getRoleName() + ") successfully!");

                if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("/login") && !redirect.contains("/demo-login")) {
                    response.sendRedirect(redirect);
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
                return;
            } else {
                request.setAttribute("error", "No demo user found. Please ensure database sample data is loaded.");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Demo login failed: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }
}
