package com.cineplex.cineplex.controller;

import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Utente;
import com.cineplex.cineplex.model.dao.DAOFactory;
import com.cineplex.cineplex.model.dao.UtenteDAO;
import com.cineplex.cineplex.model.dao.FilmDAO;
import com.cineplex.cineplex.services.config.Configuration;
import com.cineplex.cineplex.services.logservice.LogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

            // Fetch featured films
            fetchAndSetFeaturedFilms(request, daoFactory);

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
            Map<String, Object> sessionFactoryParameters = new HashMap<>();
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

                UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
                Utente existingUser = sessionUserDAO.findLoggedUser();

                if (existingUser != null) {
                    sessionUserDAO.update(utente);
                } else {
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
                }

                fetchAndSetFeaturedFilms(request, daoFactory);
                request.setAttribute("loginSuccess", true);
                request.setAttribute("viewUrl", "homeManagement/view");
            } else {
                request.setAttribute("loginError", true);
                request.setAttribute("viewUrl", "homeManagement/login");
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

            // Perform checks
            if (nome == null || nome.trim().isEmpty() || nome.length() > 50) {
                request.setAttribute("signupError", "Nome is required and must be 50 characters or less");
                request.setAttribute("viewUrl", "homeManagement/login");
                return;
            }

            if (cognome == null || cognome.trim().isEmpty() || cognome.length() > 50) {
                request.setAttribute("signupError", "Cognome is required and must be 50 characters or less");
                request.setAttribute("viewUrl", "homeManagement/login");
                return;
            }

            if (email == null || email.trim().isEmpty() || email.length() > 100 || !isValidEmail(email)) {
                request.setAttribute("signupError", "Valid email is required and must be 100 characters or less");
                request.setAttribute("viewUrl", "homeManagement/login");
                return;
            }

            if (username == null || username.trim().isEmpty() || username.length() > 45) {
                request.setAttribute("signupError", "Username is required and must be 45 characters or less");
                request.setAttribute("viewUrl", "homeManagement/login");
                return;
            }

            if (password == null || password.trim().isEmpty() || password.length() > 255) {
                request.setAttribute("signupError", "Password is required and must be 255 characters or less");
                request.setAttribute("viewUrl", "homeManagement/login");
                return;
            }

            if (utenteDAO.FindByEmail(email) != null) {
                request.setAttribute("signupError", "Email already exists");
                request.setAttribute("viewUrl", "homeManagement/login");
                return;
            }

            if (utenteDAO.FindByUsername(username) != null) {
                request.setAttribute("signupError", "Username already exists");
                request.setAttribute("viewUrl", "homeManagement/login");
                return;
            }

            try {
                Utente newUtente = utenteDAO.create(null, nome, cognome, username, email, password, newsletter, "normale");

                HttpSession session = request.getSession(true);
                session.setAttribute("loggedUser", newUtente);
                request.setAttribute("loggedOn", true);

                UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
                sessionUserDAO.create(
                        newUtente.getIdUtente(),
                        newUtente.getNome(),
                        newUtente.getCognome(),
                        newUtente.getUsername(),
                        newUtente.getEmail(),
                        newUtente.getPassword(),
                        newUtente.isNewsletter(),
                        newUtente.getTipo()
                );

                fetchAndSetFeaturedFilms(request, daoFactory);
                request.setAttribute("signupSuccess", true);
                request.setAttribute("viewUrl", "homeManagement/view");
            } catch (Exception e) {
                request.setAttribute("signupError", "Error during registration: " + e.getMessage());
                request.setAttribute("viewUrl", "homeManagement/login");
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
        DAOFactory daoFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();

            sessionUserDAO.delete(null);

            Cookie cookie = new Cookie("LoggedUser", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", false);
            request.setAttribute("loggedUser", null);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();
            fetchAndSetFeaturedFilms(request, daoFactory);
            daoFactory.commitTransaction();

            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if (daoFactory != null) daoFactory.rollbackTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Rollback Error", t);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if (daoFactory != null) daoFactory.closeTransaction();
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Close Transaction Error", t);
            }
        }
    }

    // Helper method to validate email format
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    private static void fetchAndSetFilms(HttpServletRequest request, DAOFactory daoFactory) throws Exception {
        FilmDAO filmDAO = daoFactory.getFilmDAO();
        List<Film> films = filmDAO.findAll();
        request.setAttribute("films", films);
    }

    private static void fetchAndSetFeaturedFilms(HttpServletRequest request, DAOFactory daoFactory) throws Exception {
        FilmDAO filmDAO = daoFactory.getFilmDAO();
        List<Film> featuredFilms = filmDAO.findFeaturedFilms(); // You'll need to implement this method in FilmDAO
        request.setAttribute("featuredFilms", featuredFilms);
    }

    public static void searchFilms(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory daoFactory = null;
        String applicationMessage = null;

        try {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            FilmDAO filmDAO = daoFactory.getFilmDAO();

            String title = request.getParameter("title");
            String dateStr = request.getParameter("date");
            Date date = null;
            if (dateStr != null && !dateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(dateStr);
            }

            List<Film> searchResults = filmDAO.searchFilms(title, date);

            fetchAndSetFeaturedFilms(request, daoFactory);

            daoFactory.commitTransaction();

            request.setAttribute("films", searchResults);
            request.setAttribute("searchPerformed", true);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            LogService.getApplicationLogger().log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }
}