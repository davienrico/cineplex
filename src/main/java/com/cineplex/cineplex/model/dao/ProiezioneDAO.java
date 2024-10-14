package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.mo.Proiezione;
import com.cineplex.cineplex.model.dao.mySQLJDBCImpl.ProiezioneDAOMySQLJDBCImpl.ConflictingProjectionException;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface ProiezioneDAO {
    Proiezione create(Integer idProiezione, Date dataProiezione, Time oraInizio, Integer numeroSala, Integer filmId)
            throws ConflictingProjectionException;

    void update(Proiezione proiezione) throws ConflictingProjectionException;

    void delete(Proiezione proiezione);

    Proiezione FindById(Integer idProiezione);

    List<Proiezione> FindByFilmId(Integer filmId);
}