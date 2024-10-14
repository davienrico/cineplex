package com.cineplex.cineplex.model.dao;

import com.cineplex.cineplex.model.dao.mySQLJDBCImpl.TipoAbbonamentoDAOMySQLJDBCImpl;
import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.TipoAbbonamento;

import java.math.BigDecimal;
import java.util.List;

public interface TipoAbbonamentoDAO {
    public TipoAbbonamento create(Integer idTipoAbbonamento, String tipo, BigDecimal costo, Integer durataGiorni, Integer numeroIngressi)
            throws TipoAbbonamentoDAOMySQLJDBCImpl.DuplicateTypeException;

    public void update(TipoAbbonamento tipoAbbonamento) throws TipoAbbonamentoDAOMySQLJDBCImpl.DuplicateTypeException;

    public void delete(TipoAbbonamento tipoAbbonamento);

    public List<TipoAbbonamento> findAll();

    public TipoAbbonamento findById(Integer idTipoAbbonamento);
}
