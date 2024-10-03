package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.TipoAbbonamento;

import java.math.BigDecimal;

public interface TipoAbbonamentoDAO {
        public TipoAbbonamento create(
                Integer idTipoAbbonamento,
                String tipo,
                BigDecimal costo,
                Integer durataGiorni,
                Integer numeroIngressi
        );

    public void update(TipoAbbonamento tipoabbonamento);

    public void delete(TipoAbbonamento tipoabbonamento);


}
