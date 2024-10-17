package com.cineplex.cineplex.controller;

import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.dao.DAOFactory;
import com.cineplex.cineplex.model.dao.FilmDAO;
import com.cineplex.cineplex.services.config.Configuration;
import com.cineplex.cineplex.services.logservice.LogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Level;

public class FilmManagement {

    public static void viewFilm(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory daoFactory = null;

        try {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            FilmDAO filmDAO = daoFactory.getFilmDAO();
            String filmIdStr = request.getParameter("filmId");

            if (filmIdStr != null && !filmIdStr.isEmpty()) {
                int filmId = Integer.parseInt(filmIdStr);
                Film film = filmDAO.FindById(filmId);

                if (film != null) {
                    request.setAttribute("film", film);
                    request.setAttribute("viewUrl", "filmManagement/film");
                } else {
                    request.setAttribute("viewUrl", "homeManagement/view");
                    request.setAttribute("errorMessage", "Film not found");
                }
            } else {
                request.setAttribute("viewUrl", "homeManagement/view");
                request.setAttribute("errorMessage", "Invalid film ID");
            }

            daoFactory.commitTransaction();

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