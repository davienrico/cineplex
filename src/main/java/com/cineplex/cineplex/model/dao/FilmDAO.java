package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Genere;

import java.util.Date;
import java.util.List;

public interface FilmDAO {

        public Film create(
                Integer idFilm,
                String titolo,
                Date dataInizioDisponibilita,
                Date dataFineDisponibilita,
                String percorsoLocandina,
                Date dataPubblicazione,
                Integer durataMinuti,
                String descrizione,
                String linkTrailerYT,
                String regista,
                List<Genere> generi

        );


    public void update(Film film);

    public void delete(Film film);


    public Film FindById(Integer idFilm);
    public Film FindByTitolo(String titolo);

    public Film FindByGenere(List<Genere> generi);
}
