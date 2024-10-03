package com.cineplex.cineplex.model.mo;

import java.util.List;
import java.util.ArrayList;

public class Genere {
    private Integer idGenere;
    private String nomeGenere;
    private Boolean deleted;

    // Constructors
    public Genere() {}

    public Genere(Integer idGenere, String nomeGenere, Boolean deleted) {
        this.idGenere = idGenere;
        this.nomeGenere = nomeGenere;
        this.deleted = deleted;
    }

    public Integer getIdGenere() {
        return idGenere;
    }

    public void setIdGenere(Integer idGenere) {
        this.idGenere = idGenere;
    }

    public String getNomeGenere() {
        return nomeGenere;
    }

    public void setNomeGenere(String nomeGenere) {
        this.nomeGenere = nomeGenere;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}