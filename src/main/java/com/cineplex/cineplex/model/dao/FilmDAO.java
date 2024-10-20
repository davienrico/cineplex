package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.dao.mySQLJDBCImpl.FilmDAOMySQLJDBCImpl;
import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Genere;

import java.util.Date;
import java.util.List;

public interface FilmDAO {

    Film create(Integer idFilm, String titolo, Date dataInizioDisponibilita, Date dataFineDisponibilita,
                String percorsoLocandina, Date dataPubblicazione, Integer durataMinuti, String descrizione,
                String linkTrailerYT, String regista, List<Genere> generi) throws FilmDAOMySQLJDBCImpl.DuplicateTitleException;

    void update(Film film) throws FilmDAOMySQLJDBCImpl.DuplicateTitleException;

    public void delete(Film film);

    List<Film> findAll() throws Exception;

    public Film FindById(Integer idFilm);
    List<Film> FindByTitolo(String query) throws Exception;

    public List<Film> FindByGenere(List<Genere> generi);

    List<Film> searchFilms(String title, Date date) throws Exception;

    List<Film> findFeaturedFilms() throws Exception;

}
