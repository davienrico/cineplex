package com.cineplex.cineplex.model.mo;


import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Film {
    private Integer idFilm;
    private String titolo;
    private Date dataInizioDisponibilita;
    private Date dataFineDisponibilita;
    private String percorsoLocandina;
    private Date dataPubblicazione;
    private Integer durataMinuti;
    private String descrizione;
    private String linkTrailerYt;
    private String regista;
    private Boolean deleted;
    private List<Genere> generi;

    // Constructors
    public Film() {
        this.generi = new ArrayList<>();
    }

    public Film(Integer idFilm, String titolo, Date dataInizioDisponibilita, Date dataFineDisponibilita,
                String percorsoLocandina, Date dataPubblicazione, Integer durataMinuti, String descrizione,
                String linkTrailerYt, String regista, Boolean deleted) {
        this.idFilm = idFilm;
        this.titolo = titolo;
        this.dataInizioDisponibilita = dataInizioDisponibilita;
        this.dataFineDisponibilita = dataFineDisponibilita;
        this.percorsoLocandina = percorsoLocandina;
        this.dataPubblicazione = dataPubblicazione;
        this.durataMinuti = durataMinuti;
        this.descrizione = descrizione;
        this.linkTrailerYt = linkTrailerYt;
        this.regista = regista;
        this.deleted = deleted;
        this.generi = new ArrayList<>();
    }


    public List<Genere> getGeneri() {
        return generi;
    }

    public void setGeneri(List<Genere> generi) {
        this.generi = generi;
    }

    public Integer getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Integer idFilm) {
        this.idFilm = idFilm;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Date getDataInizioDisponibilita() {
        return dataInizioDisponibilita;
    }

    public void setDataInizioDisponibilita(Date dataInizioDisponibilita) {
        this.dataInizioDisponibilita = dataInizioDisponibilita;
    }

    public Date getDataFineDisponibilita() {
        return dataFineDisponibilita;
    }

    public void setDataFineDisponibilita(Date dataFineDisponibilita) {
        this.dataFineDisponibilita = dataFineDisponibilita;
    }

    public String getPercorsoLocandina() {
        return percorsoLocandina;
    }

    public void setPercorsoLocandina(String percorsoLocandina) {
        this.percorsoLocandina = percorsoLocandina;
    }

    public Date getDataPubblicazione() {
        return dataPubblicazione;
    }

    public void setDataPubblicazione(Date dataPubblicazione) {
        this.dataPubblicazione = dataPubblicazione;
    }

    public Integer getDurataMinuti() {
        return durataMinuti;
    }

    public void setDurataMinuti(Integer durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getLinkTrailerYt() {
        return linkTrailerYt;
    }

    public void setLinkTrailerYt(String linkTrailerYt) {
        this.linkTrailerYt = linkTrailerYt;
    }

    public String getRegista() {
        return regista;
    }

    public void setRegista(String regista) {
        this.regista = regista;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}