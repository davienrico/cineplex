package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.dao.mySQLJDBCImpl.BigliettoDAOMySQLJDBCImpl.DuplicateSeatException;
import com.cineplex.cineplex.model.mo.Biglietto;
import com.cineplex.cineplex.model.mo.Proiezione;
import com.cineplex.cineplex.model.mo.Utente;

public interface BigliettoDAO {
        Biglietto create(Integer idBiglietto, String posto, Boolean ingressoAbbonamento, Utente utente, Proiezione proiezione)
                throws DuplicateSeatException;

        void update(Biglietto biglietto) throws DuplicateSeatException;

        public void delete(Biglietto biglietto);

        public Biglietto FindById(Integer idBiglietto);

        public Biglietto FindByUserId(Utente utente);

        public Biglietto FindByProiezione(Proiezione proiezione);


}
