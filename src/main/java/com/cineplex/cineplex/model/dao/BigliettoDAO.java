package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.mo.Biglietto;
import com.cineplex.cineplex.model.mo.Proiezione;
import com.cineplex.cineplex.model.mo.Utente;

public interface BigliettoDAO {
        public Biglietto create(
                Integer idBiglietto,
                String Posto,
                Boolean ingressoAbbonamento,
                Utente utente,
                Proiezione proiezione
        );

        public void update(Biglietto bigletto);

        public void delete(Biglietto biglietto);

        public Biglietto FindById(Integer idBiglietto);

        public Biglietto FindByUserId(Utente utente);

        public Biglietto FindByProiezione(Proiezione proiezione);


}
