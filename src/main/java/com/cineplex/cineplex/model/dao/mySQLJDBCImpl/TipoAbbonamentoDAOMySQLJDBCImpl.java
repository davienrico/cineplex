package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.TipoAbbonamentoDAO;
import com.cineplex.cineplex.model.mo.TipoAbbonamento;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TipoAbbonamentoDAOMySQLJDBCImpl implements TipoAbbonamentoDAO {

    private Connection connection;

    public TipoAbbonamentoDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public TipoAbbonamento create(Integer idTipoAbbonamento, String tipo, BigDecimal costo, Integer durataGiorni, Integer numeroIngressi)
            throws DuplicateTypeException {
        PreparedStatement ps;
        TipoAbbonamento tipoAbbonamento = new TipoAbbonamento();
        try {
            // Check if the tipo already exists
            if (isTipoExist(tipo)) {
                throw new DuplicateTypeException("Il tipo di abbonamento '" + tipo + "' esiste già.");
            }

            String sql = "INSERT INTO tipo_abbonamento (tipo, costo, durata_giorni, numero_ingressi) VALUES (?,?,?,?)";

            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setString(i++, tipo);
            ps.setBigDecimal(i++, costo);
            ps.setInt(i++, durataGiorni);
            ps.setInt(i++, numeroIngressi);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idTipoAbbonamento = rs.getInt(1);
            }

            tipoAbbonamento.setIdTipoAbbonamento(idTipoAbbonamento);
            tipoAbbonamento.setTipo(tipo);
            tipoAbbonamento.setCosto(costo);
            tipoAbbonamento.setDurataGiorni(durataGiorni);
            tipoAbbonamento.setNumeroIngressi(numeroIngressi);
            tipoAbbonamento.setDeleted(false);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tipoAbbonamento;
    }

    @Override
    public void update(TipoAbbonamento tipoAbbonamento) throws DuplicateTypeException {
        PreparedStatement ps;
        try {
            // Check if the new tipo already exists (excluding this record)
            if (isTipoExistExcludingThis(tipoAbbonamento.getTipo(), tipoAbbonamento.getIdTipoAbbonamento())) {
                throw new DuplicateTypeException("Il tipo di abbonamento '" + tipoAbbonamento.getTipo() + "' esiste già.");
            }

            String sql = "UPDATE tipo_abbonamento SET tipo = ?, costo = ?, durata_giorni = ?, " +
                    "numero_ingressi = ?, deleted = ? WHERE id_tipo_abbonamento = ?";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, tipoAbbonamento.getTipo());
            ps.setBigDecimal(i++, tipoAbbonamento.getCosto());
            ps.setInt(i++, tipoAbbonamento.getDurataGiorni());
            ps.setInt(i++, tipoAbbonamento.getNumeroIngressi());
            ps.setString(i++, tipoAbbonamento.isDeleted() ? "y" : "n");
            ps.setInt(i++, tipoAbbonamento.getIdTipoAbbonamento());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(TipoAbbonamento tipoAbbonamento) {
        PreparedStatement ps;
        try {
            String sql = "UPDATE tipo_abbonamento SET deleted = 'y' WHERE id_tipo_abbonamento = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, tipoAbbonamento.getIdTipoAbbonamento());
            ps.executeUpdate();
            tipoAbbonamento.setDeleted(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TipoAbbonamento> findAll() {
        PreparedStatement ps;
        List<TipoAbbonamento> tipiAbbonamento = new ArrayList<>();

        try {
            String sql = "SELECT * FROM tipo_abbonamento WHERE deleted = 'n'";

            ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                tipiAbbonamento.add(read(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tipiAbbonamento;
    }

    public TipoAbbonamento findById(Integer idTipoAbbonamento) {
        PreparedStatement ps;
        TipoAbbonamento tipoAbbonamento = null;

        try {
            String sql = "SELECT * FROM tipo_abbonamento WHERE id_tipo_abbonamento = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, idTipoAbbonamento);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                tipoAbbonamento = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tipoAbbonamento;
    }

    private TipoAbbonamento read(ResultSet rs) throws SQLException {
        TipoAbbonamento tipoAbbonamento = new TipoAbbonamento();
        tipoAbbonamento.setIdTipoAbbonamento(rs.getInt("id_tipo_abbonamento"));
        tipoAbbonamento.setTipo(rs.getString("tipo"));
        tipoAbbonamento.setCosto(rs.getBigDecimal("costo"));
        tipoAbbonamento.setDurataGiorni(rs.getInt("durata_giorni"));
        tipoAbbonamento.setNumeroIngressi(rs.getInt("numero_ingressi"));
        tipoAbbonamento.setDeleted(rs.getString("deleted").equals("y"));
        return tipoAbbonamento;
    }

    private boolean isTipoExist(String tipo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tipo_abbonamento WHERE tipo = ? AND deleted = 'n'";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, tipo);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private boolean isTipoExistExcludingThis(String tipo, int idTipoAbbonamento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tipo_abbonamento WHERE tipo = ? AND id_tipo_abbonamento != ? AND deleted = 'n'";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, tipo);
        ps.setInt(2, idTipoAbbonamento);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public static class DuplicateTypeException extends Exception {
        public DuplicateTypeException(String message) {
            super(message);
        }
    }
}