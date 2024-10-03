package com.cineplex.cineplex.model.mo;


import java.util.Date;

public class Recensione {
    private Integer idRecensione;
    private Integer valutazione;
    private String titoloRecensione;
    private String testo;
    private Date dataScrittura;
    private Utente utente;
    private Film film;
    private Boolean deleted;

    // Constructors
    public Recensione() {}

    public Recensione(Integer idRecensione, Integer valutazione, String titoloRecensione, String testo,
                      Date dataScrittura, Utente utente, Film film, Boolean deleted) {
        this.idRecensione = idRecensione;
        this.valutazione = valutazione;
        this.titoloRecensione = titoloRecensione;
        this.testo = testo;
        this.dataScrittura = dataScrittura;
        this.utente = utente;
        this.film = film;
        this.deleted = deleted;
    }

    // Getters and Setters
    public Integer getIdRecensione() {
        return idRecensione;
    }

    public void setIdRecensione(Integer idRecensione) {
        this.idRecensione = idRecensione;
    }

    public Integer getValutazione() {
        return valutazione;
    }

    public void setValutazione(Integer valutazione) {
        this.valutazione = valutazione;
    }

    public String getTitoloRecensione() {
        return titoloRecensione;
    }

    public void setTitoloRecensione(String titoloRecensione) {
        this.titoloRecensione = titoloRecensione;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Date getDataScrittura() {
        return dataScrittura;
    }

    public void setDataScrittura(Date dataScrittura) {
        this.dataScrittura = dataScrittura;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}