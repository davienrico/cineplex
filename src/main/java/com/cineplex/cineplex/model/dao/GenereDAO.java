package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Genere;

import java.util.List;

public interface GenereDAO {
        public Genere create(
                int idGenere,
                String nomeGenere,
                List<Film> films
        );

        public void update(Genere genere);

        public void delete(Genere genere);

        public Genere FindById(Integer idGenere);

        public Genere FindByFilm(List<Film> films);

        public Genere FindByNomeGenere(String nomeGenere);
}
