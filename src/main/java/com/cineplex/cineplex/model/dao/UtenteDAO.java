package com.cineplex.cineplex.model.dao;

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
        );

    public void update(Utente utente);

    public void delete(Utente utente);

    public Utente findLoggedUser();

    public Utente FindById(Integer idUtente);

    public Utente FindByUsername(String username);

    public Utente FindByEmail(String email);


}
