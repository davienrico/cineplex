package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.Proiezione;

import java.sql.Time;
import java.util.Date;

public interface ProiezioneDAO {
        public Proiezione create(
                Integer idProiezione,
                Date dataProiezione,
                Time oraInizio,
                Integer numeroSala,
                Integer filmId

        );

    public void update(Proiezione proiezione);

    public void delete(Proiezione proiezione);

    public Proiezione FindById(Integer idProiezione);

    public Proiezione FindByDateTime(Date dataProiezione, Time oraInizio);

    public Proiezione FindBySala(Integer numeroSala);

    public Proiezione FindByFilmId(Integer filmId);
}
