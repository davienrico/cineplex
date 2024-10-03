package com.cineplex.cineplex.model.mo;


public class Utente {
    private Integer idUtente;
    private String nome;
    private String cognome;

    private String username;

    private String email;
    private String password;
    private Boolean newsletter;
    private String tipo;
    private Boolean banned;
    private Boolean deleted;


    public Utente() {}

    //costruttore
    public Utente(Integer idUtente, String nome, String cognome, String username,String email, String password,
                  Boolean newsletter, String tipo, Boolean banned, Boolean deleted) {
        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.password = password;
        this.newsletter = newsletter;
        this.tipo = tipo;
        this.banned = banned;
        this.deleted = deleted;
    }

    // get & set
    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isNewsletter() {
        return newsletter;
    }

    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username; }
}