package com.cineplex.cineplex.controller;

import com.cineplex.cineplex.model.mo.Utente;
import com.cineplex.cineplex.model.dao.DAOFactory;
import com.cineplex.cineplex.model.dao.UtenteDAO;
import com.cineplex.cineplex.services.config.Configuration;
import com.cineplex.cineplex.services.logservice.LogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeManagement {

    public static void view(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            // Check for loggedUser cookie and set loggedOn attribute
            Cookie[] cookies = request.getCookies();
            boolean isLoggedOn = false;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("loggedUser") && !cookie.getValue().isEmpty()) {
                        isLoggedOn = true;
                        break;
                    }
                }
            }
            request.setAttribute("loggedOn", isLoggedOn);

            // Add any other logic needed for the home page view here

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            LogService.getApplicationLogger().log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void loginView(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("viewUrl", "homeManagement/login");
    }

    public static void login(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = utenteDAO.authenticate(username, password);

            if (utente != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("loggedUser", utente);
                request.setAttribute("loggedOn", true);

                // Use the sessionDAOFactory to set the cookie
                UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
                sessionUserDAO.create(
                        utente.getIdUtente(),
                        utente.getNome(),
                        utente.getCognome(),
                        utente.getUsername(),
                        utente.getEmail(),
                        utente.getPassword(),
                        utente.isNewsletter(),
                        utente.getTipo()
                );

                request.setAttribute("viewUrl", "homeManagement/view");
            } else {
                applicationMessage = "Invalid username or password";
                request.setAttribute("loggedOn", false);
                request.setAttribute("viewUrl", "homeManagement/login");
                request.setAttribute("applicationMessage", applicationMessage);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

        } catch (Exception e) {
            LogService.getApplicationLogger().log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void signup(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            boolean newsletter = request.getParameter("newsletter") != null;

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();

            try {
                Utente newUtente = utenteDAO.create(null, nome, cognome, username, email, password, newsletter, "normale");

                HttpSession session = request.getSession(true);
                request.setAttribute("loggedOn", true);  // Add this line

                // Set cookie
                Cookie cookie = new Cookie("loggedUser", newUtente.getUsername());
                cookie.setMaxAge(3600); // 1 hour
                response.addCookie(cookie);

                request.setAttribute("viewUrl", "homeManagement/view");
            } catch (Exception e) {
                applicationMessage = "Error during registration: " + e.getMessage();
                request.setAttribute("loggedOn", false);  // Add this line
                request.setAttribute("viewUrl", "homeManagement/login");
                request.setAttribute("applicationMessage", applicationMessage);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

        } catch (Exception e) {
            LogService.getApplicationLogger().log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }
    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();

            // Delete the user cookie
            sessionUserDAO.delete(null);

            // Manually delete the LoggedUser cookie
            Cookie cookie = new Cookie("LoggedUser", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            // Invalidate the session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            sessionDAOFactory.commitTransaction();

            // Clear session attributes
            request.setAttribute("loggedOn", false);
            request.setAttribute("loggedUser", null);

            // Set view url for redirection
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                // Log this exception as well
                logger.log(Level.SEVERE, "Rollback Error", t);
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
                // Log this exception
                logger.log(Level.SEVERE, "Close Transaction Error", t);
            }
        }
    }
}