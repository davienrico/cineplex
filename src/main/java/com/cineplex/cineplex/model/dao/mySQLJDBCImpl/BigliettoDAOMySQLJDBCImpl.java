package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.BigliettoDAO;
import com.cineplex.cineplex.model.mo.Biglietto;
import com.cineplex.cineplex.model.mo.Proiezione;
import com.cineplex.cineplex.model.mo.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BigliettoDAOMySQLJDBCImpl implements BigliettoDAO {

    private Connection connection;

    public BigliettoDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Biglietto create(Integer idBiglietto, String posto, Boolean ingressoAbbonamento, Utente utente, Proiezione proiezione)
            throws DuplicateSeatException {
        PreparedStatement ps;
        Biglietto biglietto = new Biglietto();
        try {
            // Check if the seat is already taken for this projection
            if (isSeatTaken(posto, proiezione.getIdProiezione())) {
                throw new DuplicateSeatException("Il posto " + posto + " è già occupato per questa proiezione.");
            }

            String sql
                    = " INSERT INTO biglietto"
                    + " (posto, ingresso_abbonamento, utente_id, proiezione_id)"
                    + " VALUES (?,?,?,?)";

            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setString(i++, posto);
            ps.setString(i++, ingressoAbbonamento ? "y" : "n");
            ps.setInt(i++, utente.getIdUtente());
            ps.setInt(i++, proiezione.getIdProiezione());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idBiglietto = rs.getInt(1);
            }

            biglietto.setIdBiglietto(idBiglietto);
            biglietto.setPosto(posto);
            biglietto.setIngressoAbbonamento(ingressoAbbonamento);
            biglietto.setUtente(utente);
            biglietto.setProiezione(proiezione);
            biglietto.setDeleted(false);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return biglietto;
    }

    @Override
    public void update(Biglietto biglietto) throws DuplicateSeatException {
        PreparedStatement ps;
        try {
            // Check if the new seat is already taken for this projection
            if (isSeatTaken(biglietto.getPosto(), biglietto.getProiezione().getIdProiezione())) {
                throw new DuplicateSeatException("Il posto " + biglietto.getPosto() + " è già occupato per questa proiezione.");
            }

            String sql
                    = " UPDATE biglietto"
                    + " SET posto = ?,"
                    + " ingresso_abbonamento = ?,"
                    + " utente_id = ?,"
                    + " proiezione_id = ?,"
                    + " deleted = ?"
                    + " WHERE"
                    + " id_biglietto = ?";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, biglietto.getPosto());
            ps.setString(i++, biglietto.isIngressoAbbonamento() ? "y" : "n");
            ps.setInt(i++, biglietto.getUtente().getIdUtente());
            ps.setInt(i++, biglietto.getProiezione().getIdProiezione());
            ps.setString(i++, biglietto.isDeleted() ? "y" : "n");
            ps.setInt(i++, biglietto.getIdBiglietto());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Biglietto biglietto) {
        PreparedStatement ps;
        try {
            String sql
                    = " UPDATE biglietto"
                    + " SET deleted = 'y'"
                    + " WHERE"
                    + " id_biglietto = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, biglietto.getIdBiglietto());
            ps.executeUpdate();
            biglietto.setDeleted(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Biglietto FindById(Integer idBiglietto) {
        PreparedStatement ps;
        Biglietto biglietto = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM biglietto"
                    + " WHERE"
                    + " id_biglietto = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, idBiglietto);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                biglietto = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return biglietto;
    }

    @Override
    public Biglietto FindByUserId(Utente utente) {
        PreparedStatement ps;
        Biglietto biglietto = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM biglietto"
                    + " WHERE"
                    + " utente_id = ?"
                    + " AND deleted = 'n'"
                    + " ORDER BY id_biglietto DESC"
                    + " LIMIT 1";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, utente.getIdUtente());

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                biglietto = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return biglietto;
    }

    @Override
    public Biglietto FindByProiezione(Proiezione proiezione) {
        PreparedStatement ps;
        Biglietto biglietto = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM biglietto"
                    + " WHERE"
                    + " proiezione_id = ?"
                    + " AND deleted = 'n'"
                    + " ORDER BY id_biglietto DESC"
                    + " LIMIT 1";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, proiezione.getIdProiezione());

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                biglietto = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return biglietto;
    }

    private Biglietto read(ResultSet rs) {
        Biglietto biglietto = new Biglietto();
        try {
            biglietto.setIdBiglietto(rs.getInt("id_biglietto"));
            biglietto.setPosto(rs.getString("posto"));
            biglietto.setIngressoAbbonamento(rs.getString("ingresso_abbonamento").equals("y"));

            Utente utente = new Utente();
            utente.setIdUtente(rs.getInt("utente_id"));
            biglietto.setUtente(utente);

            Proiezione proiezione = new Proiezione();
            proiezione.setIdProiezione(rs.getInt("proiezione_id"));
            biglietto.setProiezione(proiezione);

            biglietto.setDeleted(rs.getString("deleted").equals("y"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return biglietto;
    }

    private boolean isSeatTaken(String posto, int proiezioneId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM biglietto WHERE posto = ? AND proiezione_id = ? AND deleted = 'n'";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, posto);
        ps.setInt(2, proiezioneId);

        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public static class DuplicateSeatException extends Exception {
        public DuplicateSeatException(String message) {
            super(message);
        }
    }
}