package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.dao.mySQLJDBCImpl.RecensioneDAOMySQLJDBCImpl;
import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Recensione;
import com.cineplex.cineplex.model.mo.Utente;

import java.util.Date;
import java.util.List;

public interface RecensioneDAO {
    Recensione create(Integer idRecensione, Integer valutazione, String titoloRecensione, String testo,
                      Utente utente, Film film) throws RecensioneDAOMySQLJDBCImpl.DuplicateReviewException;

    void update(Recensione recensione);


    public void delete(Recensione recensione);

    public Recensione FindById(Integer idRecensione);

    public List<Recensione> FindByUserId(Utente utente);

    public List<Recensione> FindByFilm(Film film);
}
