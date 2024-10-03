package com.cineplex.cineplex.model.mo;

import java.util.Date;

public class Abbonamento {
    private Integer idAbbonamento;
    private Date dataSottoscrizione;
    private Utente utenteId;
    private TipoAbbonamento tipoAbbonamentoId;
    private Boolean deleted;

    // costruttori
    public Abbonamento() {}

    public Abbonamento(Integer idAbbonamento, Date dataSottoscrizione, Utente utenteId, TipoAbbonamento tipoAbbonamentoId, Boolean deleted) {
        this.idAbbonamento = idAbbonamento;
        this.dataSottoscrizione = dataSottoscrizione;
        this.utenteId = utenteId;
        this.tipoAbbonamentoId = tipoAbbonamentoId;
        this.deleted = deleted;
    }

    // get  & set
    public Integer getIdAbbonamento() {
        return idAbbonamento;
    }

    public void setIdAbbonamento(Integer idAbbonamento) {
        this.idAbbonamento = idAbbonamento;
    }

    public Date getDataSottoscrizione() {
        return dataSottoscrizione;
    }

    public void setDataSottoscrizione(Date dataSottoscrizione) {
        this.dataSottoscrizione = dataSottoscrizione;
    }

    public Utente getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(Utente utenteId) {
        this.utenteId = utenteId;
    }

    public TipoAbbonamento getTipoAbbonamentoId() {
        return tipoAbbonamentoId;
    }

    public void setTipoAbbonamentoId(TipoAbbonamento tipoAbbonamentoId) {
        this.tipoAbbonamentoId = tipoAbbonamentoId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}