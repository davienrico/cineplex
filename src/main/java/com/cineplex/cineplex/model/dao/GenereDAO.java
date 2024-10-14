package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.dao.mySQLJDBCImpl.GenereDAOMySQLJDBCImpl;
import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Genere;

import java.util.List;

public interface GenereDAO {
        Genere create(int idGenere, String nomeGenere, List<Film> films) throws GenereDAOMySQLJDBCImpl.DuplicateGenreException;

        void update(Genere genere) throws GenereDAOMySQLJDBCImpl.DuplicateGenreException;


        public void delete(Genere genere);

        public Genere FindById(Integer idGenere);

        public List<Genere> FindByFilm(List<Film> films);

        public Genere FindByNomeGenere(String nomeGenere);
}
