package com.cineplex.cineplex.model.dao;

import java.util.Date;
import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.TipoAbbonamento;
import com.cineplex.cineplex.model.mo.Utente;

import java.util.Date;

public interface AbbonamentoDAO {

        public Abbonamento create(
                Integer idAbbonamento,
                Date dataSottoscrizione,
                Utente utenteId,
                TipoAbbonamento tipoAbbonamentoId
        );

        public void update(Abbonamento abbonamento);

        public void delete(Abbonamento abbonamento);

        public Abbonamento FindById(Integer idAbbonamento);


        public Abbonamento FindByUserId(Utente utente);

        public Abbonamento FindByDate(Date dataSottoscrizione);



}
