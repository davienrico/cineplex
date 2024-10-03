package com.cineplex.cineplex.model.dao.CookieImpl;

import com.cineplex.cineplex.model.dao.UtenteDAO;
import com.cineplex.cineplex.model.mo.Utente;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UtenteDAOCookieImpl implements UtenteDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UtenteDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Utente create(Integer idUtente, String nome, String cognome, String username, String email, String password, Boolean newsletter, String tipo) {
        Utente loggedUser = new Utente();
        loggedUser.setIdUtente(idUtente);
        loggedUser.setUsername(username);
        loggedUser.setNewsletter(newsletter);
        loggedUser.setTipo(tipo);

        Cookie cookie;
        cookie = new Cookie("LoggedUser",encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);


        return loggedUser;
    }




    @Override
    public void update(Utente loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void delete(Utente loggedUser) {
        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }


    public Utente findLoggedUser(){
        Cookie[] cookies = request.getCookies();
        Utente loggedUser = null;

        if(cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }
        return loggedUser;
    }
    @Override
    public Utente FindById(Integer idUtente) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Utente FindByUsername(String username) {
        throw new UnsupportedOperationException("Not supported");

    }

    @Override
    public Utente FindByEmail(String email) {
        throw new UnsupportedOperationException("Not supported");

    }

    private String encode(Utente loggedUser){
        return loggedUser.getIdUtente() + "#" + loggedUser.getUsername() + "#" + loggedUser.isNewsletter() + "#" + loggedUser.getTipo();
    }


    private Utente decode(String encodedLoggedUser) {
        Utente loggedUser = new Utente();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setIdUtente(Integer.parseInt(values[0]));
        loggedUser.setUsername(values[1]);
        loggedUser.setNewsletter(Boolean.parseBoolean(values[2]));
        loggedUser.setTipo(values[3]);

        return loggedUser;

    }
}
