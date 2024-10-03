package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Recensione;
import com.cineplex.cineplex.model.mo.Utente;

import java.util.Date;

public interface RecensioneDAO {
        public Recensione create(
                Integer idRecensione,
                Integer valutazione,
                String titoloRecensione,
                String testo,
                Date dataScrittura,
                Utente utente,
                Film film
        );

    public void update(Recensione recensione);

    public void delete(Recensione recensione);

    public Recensione FindById(Integer idRecensione);

    public Recensione FindByUserId(Utente utente);

    public Recensione FindByFilm(Film film);
}
