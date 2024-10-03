package com.cineplex.cineplex.model.mo;

public class Biglietto {
    private Integer idBiglietto;
    private String posto;
    private Boolean ingressoAbbonamento;
    private Utente utente;
    private Proiezione proiezione;
    private Boolean deleted;

    // Constructors
    public Biglietto() {}

    public Biglietto(Integer idBiglietto, String posto, Boolean ingressoAbbonamento, Utente utente, Proiezione proiezione, Boolean deleted) {
        this.idBiglietto = idBiglietto;
        this.posto = posto;
        this.ingressoAbbonamento = ingressoAbbonamento;
        this.utente = utente;
        this.proiezione = proiezione;
        this.deleted = deleted;
    }

    // Getters and Setters
    public Integer getIdBiglietto() {
        return idBiglietto;
    }

    public void setIdBiglietto(Integer idBiglietto) {
        this.idBiglietto = idBiglietto;
    }

    public String getPosto() {
        return posto;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public Boolean isIngressoAbbonamento() {
        return ingressoAbbonamento;
    }

    public void setIngressoAbbonamento(Boolean ingressoAbbonamento) {
        this.ingressoAbbonamento = ingressoAbbonamento;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Proiezione getProiezione() {
        return proiezione;
    }

    public void setProiezione(Proiezione proiezione) {
        this.proiezione = proiezione;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}