package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.dao.exceptions.UserExceptions.DuplicateUsernameException;
import com.cineplex.cineplex.model.dao.exceptions.UserExceptions.DuplicateEmailException;

import com.cineplex.cineplex.model.dao.mySQLJDBCImpl.UtenteDAOMySQLJDBCImpl;
import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.Utente;

public interface UtenteDAO {
        public Utente create(
                Integer idUtente,
                String nome,
                String cognome,
                String username,
                String email,
                String password,
                Boolean newsletter,
                String tipo
        ) throws DuplicateUsernameException, DuplicateEmailException;


    Utente authenticate(String username, String password);

    public void update(Utente utente)throws DuplicateUsernameException, DuplicateEmailException;

    public void delete(Utente utente);


    public void banUtente(int idUtente);

    public void unbanUtente(int idUtente);


    public Utente findLoggedUser();

    public Utente FindById(Integer idUtente);

    public Utente FindByUsername(String username);

    public Utente FindByEmail(String email);


}
