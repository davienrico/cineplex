package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.AbbonamentoDAO;
import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.TipoAbbonamento;
import com.cineplex.cineplex.model.mo.Utente;

import java.sql.*;
import java.util.Date;

public class AbbonamentoDAOMySQLJDBCImpl implements AbbonamentoDAO {

    private Connection connection;

    public AbbonamentoDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Abbonamento create(Integer idAbbonamento, Date dataSottoscrizione, Utente utenteId, TipoAbbonamento tipoAbbonamentoId) {
        PreparedStatement ps;
        Abbonamento abbonamento = new Abbonamento();
        try {
            String sql
                    = " INSERT INTO abbonamento"
                    + " (data_sottoscrizione, utente_id, tipo_abbonamento_id)"
                    + " VALUES (?,?,?)";

            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setDate(i++, new java.sql.Date(dataSottoscrizione.getTime()));
            ps.setInt(i++, utenteId.getIdUtente());
            ps.setInt(i++, tipoAbbonamentoId.getIdTipoAbbonamento());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idAbbonamento = rs.getInt(1);
            }

            abbonamento.setIdAbbonamento(idAbbonamento);
            abbonamento.setDataSottoscrizione(dataSottoscrizione);
            abbonamento.setUtenteId(utenteId);
            abbonamento.setTipoAbbonamentoId(tipoAbbonamentoId);
            abbonamento.setDeleted(false);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return abbonamento;
    }

    @Override
    public void update(Abbonamento abbonamento) {
        PreparedStatement ps;
        try {
            String sql
                    = " UPDATE abbonamento"
                    + " SET data_sottoscrizione = ?,"
                    + " utente_id = ?,"
                    + " tipo_abbonamento_id = ?,"
                    + " deleted = ?"
                    + " WHERE"
                    + " id_abbonamento = ?";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setDate(i++, new java.sql.Date(abbonamento.getDataSottoscrizione().getTime()));
            ps.setInt(i++, abbonamento.getUtenteId().getIdUtente());
            ps.setInt(i++, abbonamento.getTipoAbbonamentoId().getIdTipoAbbonamento());
            ps.setString(i++, abbonamento.isDeleted() ? "y" : "n");
            ps.setInt(i++, abbonamento.getIdAbbonamento());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Abbonamento abbonamento) {
        PreparedStatement ps;
        try {
            String sql
                    = " UPDATE abbonamento"
                    + " SET deleted = 'y'"
                    + " WHERE"
                    + " id_abbonamento = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, abbonamento.getIdAbbonamento());
            ps.executeUpdate();
            abbonamento.setDeleted(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Abbonamento FindById(Integer idAbbonamento) {
        PreparedStatement ps;
        Abbonamento abbonamento = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM abbonamento"
                    + " WHERE"
                    + " id_abbonamento = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, idAbbonamento);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                abbonamento = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return abbonamento;
    }

    @Override
    public Abbonamento FindByUserId(Utente utente) {
        PreparedStatement ps;
        Abbonamento abbonamento = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM abbonamento"
                    + " WHERE"
                    + " utente_id = ?"
                    + " AND deleted = 'n'"
                    + " LIMIT 1";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, utente.getIdUtente());

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                abbonamento = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return abbonamento;
    }

    @Override
    public Abbonamento FindByDate(Date dataSottoscrizione) {
        return null;
    }

    private Abbonamento read(ResultSet rs) {
        Abbonamento abbonamento = new Abbonamento();
        try {
            abbonamento.setIdAbbonamento(rs.getInt("id_abbonamento"));
            abbonamento.setDataSottoscrizione(rs.getDate("data_sottoscrizione"));

            Utente utente = new Utente();
            utente.setIdUtente(rs.getInt("utente_id"));
            abbonamento.setUtenteId(utente);

            TipoAbbonamento tipoAbbonamento = new TipoAbbonamento();
            tipoAbbonamento.setIdTipoAbbonamento(rs.getInt("tipo_abbonamento_id"));
            abbonamento.setTipoAbbonamentoId(tipoAbbonamento);

            abbonamento.setDeleted(rs.getString("deleted").equals("y"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return abbonamento;
    }
}