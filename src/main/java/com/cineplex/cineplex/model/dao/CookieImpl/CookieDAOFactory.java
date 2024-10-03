package com.cineplex.cineplex.model.dao.CookieImpl;

import com.cineplex.cineplex.model.dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class CookieDAOFactory extends DAOFactory{

    private Map factoryPrameters;

    private HttpServletRequest request;
    private HttpServletResponse response;


    public CookieDAOFactory(Map factoryPrameters){this.factoryPrameters = factoryPrameters;}

    @Override
    public void beginTransaction() {

        try {
            this.request = (HttpServletRequest) factoryPrameters.get("request");
            this.response = (HttpServletResponse) factoryPrameters.get("response");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commitTransaction() {}

    @Override
    public void rollbackTransaction() {}

    @Override
    public void closeTransaction() {}

    @Override
    public AbbonamentoDAO getAbbonamentoDAO() {
        return null;
    }

    @Override
    public BigliettoDAO getBigliettoDAO() {
        return null;
    }

    @Override
    public FilmDAO getFilmDAO() {
        return null;
    }

    @Override
    public GenereDAO getGenereDAO() {
        return null;
    }

    @Override
    public ProiezioneDAO getProiezioneDAO() {
        return null;
    }

    @Override
    public RecensioneDAO getRecensioneDAO() {
        return null;
    }

    @Override
    public TipoAbbonamentoDAO getTipoAbbonamentoDAO() {
        return null;
    }

    @Override
    public UtenteDAO getUtenteDAO() {
        return new UtenteDAOCookieImpl(request,response);
    }





}
