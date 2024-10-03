package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.dao.CookieImpl.CookieDAOFactory;
import com.cineplex.cineplex.model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;

import java.util.Map;

public abstract class DAOFactory {

    // List of DAO types supported by the factory
    public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";
    public static final String COOKIEIMPL= "CookieImpl";

    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();

    public abstract AbbonamentoDAO getAbbonamentoDAO();

    public abstract BigliettoDAO getBigliettoDAO();

    public abstract FilmDAO getFilmDAO();


    public abstract GenereDAO getGenereDAO();

    public abstract ProiezioneDAO getProiezioneDAO();

    public abstract RecensioneDAO getRecensioneDAO();

    public abstract TipoAbbonamentoDAO getTipoAbbonamentoDAO();

    public abstract UtenteDAO getUtenteDAO();

    public static DAOFactory getDAOFactory(String whichFactory,Map factoryParameters) {

        if (whichFactory.equals(MYSQLJDBCIMPL)) {
            return new MySQLJDBCDAOFactory(factoryParameters);
        } else if (whichFactory.equals(COOKIEIMPL)) {
            return new CookieDAOFactory(factoryParameters);
        } else {
            return null;
        }
    }
}