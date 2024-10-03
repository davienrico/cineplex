package com.cineplex.cineplex.model.mo;


import java.util.Date;
import java.sql.Time;

public class Proiezione {
    private Integer idProiezione;
    private Date dataProiezione;
    private Time oraInizio;
    private Integer numeroSala;
    private Integer filmId;
    private Boolean deleted;

    // costruttori
    public Proiezione() {}

    public Proiezione(Integer idProiezione, Date dataProiezione, Time oraInizio, Integer numeroSala, Integer filmId, Boolean deleted) {
        this.idProiezione = idProiezione;
        this.dataProiezione = dataProiezione;
        this.oraInizio = oraInizio;
        this.numeroSala = numeroSala;
        this.filmId = filmId;
        this.deleted = deleted;
    }

    // get & set
    public Integer getIdProiezione() {
        return idProiezione;
    }

    public void setIdProiezione(Integer idProiezione) {
        this.idProiezione = idProiezione;
    }

    public Date getDataProiezione() {
        return dataProiezione;
    }

    public void setDataProiezione(Date dataProiezione) {
        this.dataProiezione = dataProiezione;
    }

    public Time getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(Time oraInizio) {
        this.oraInizio = oraInizio;
    }

    public Integer getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(Integer numeroSala) {
        this.numeroSala = numeroSala;
    }

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}