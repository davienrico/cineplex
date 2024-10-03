package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.UtenteDAO;
import com.cineplex.cineplex.model.mo.Utente;

public class UtenteDAOMySQLJDBCImpl implements UtenteDAO {
    @Override
    public Utente create(Integer idUtente, String nome, String cognome, String username, String email, String password, Boolean newsletter, String tipo) {
        return null;
    }

    @Override
    public void update(Utente utente) {

    }

    @Override
    public void delete(Utente utente) {

    }

    @Override
    public Utente findLoggedUser() {
        return null;
    }

    @Override
    public Utente FindById(Integer idUtente) {
        return null;
    }

    @Override
    public Utente FindByUsername(String username) {
        return null;
    }

    @Override
    public Utente FindByEmail(String email) {
        return null;
    }
}
