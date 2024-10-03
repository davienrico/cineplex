package com.cineplex.cineplex.model.mo;

import java.math.BigDecimal;

public class TipoAbbonamento {
    private Integer idTipoAbbonamento;
    private String tipo;
    private BigDecimal costo;
    private Integer durataGiorni;
    private Integer numeroIngressi;
    private Boolean deleted;

    // costruttori
    public TipoAbbonamento() {}

    public TipoAbbonamento(Integer idTipoAbbonamento, String tipo, BigDecimal costo, Integer durataGiorni, Integer numeroIngressi, Boolean deleted) {
        this.idTipoAbbonamento = idTipoAbbonamento;
        this.tipo = tipo;
        this.costo = costo;
        this.durataGiorni = durataGiorni;
        this.numeroIngressi = numeroIngressi;
        this.deleted = deleted;
    }

    // get & set
    public Integer getIdTipoAbbonamento() {
        return idTipoAbbonamento;
    }

    public void setIdTipoAbbonamento(Integer idTipoAbbonamento) {
        this.idTipoAbbonamento = idTipoAbbonamento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Integer getDurataGiorni() {
        return durataGiorni;
    }

    public void setDurataGiorni(Integer durataGiorni) {
        this.durataGiorni = durataGiorni;
    }

    public Integer getNumeroIngressi() {
        return numeroIngressi;
    }

    public void setNumeroIngressi(Integer numeroIngressi) {
        this.numeroIngressi = numeroIngressi;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}