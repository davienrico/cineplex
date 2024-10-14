package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.ProiezioneDAO;
import com.cineplex.cineplex.model.mo.Proiezione;

import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class ProiezioneDAOMySQLJDBCImpl implements ProiezioneDAO {

    private Connection connection;

    public ProiezioneDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Proiezione create(Integer idProiezione, Date dataProiezione, Time oraInizio, Integer numeroSala, Integer filmId)
            throws ConflictingProjectionException {
        PreparedStatement ps;
        Proiezione proiezione = new Proiezione();
        try {
            // Check for conflicting projections
            if (isConflictingProjection(dataProiezione, oraInizio, numeroSala, null)) {
                throw new ConflictingProjectionException("Esiste già una proiezione in conflitto per questa sala e orario.");
            }

            String sql = "INSERT INTO proiezione (data_proiezione, ora_inizio, numero_sala, film_id) VALUES (?,?,?,?)";

            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, new java.sql.Date(dataProiezione.getTime()));
            ps.setTime(2, oraInizio);
            ps.setInt(3, numeroSala);
            ps.setInt(4, filmId);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idProiezione = rs.getInt(1);
            }

            proiezione.setIdProiezione(idProiezione);
            proiezione.setDataProiezione(dataProiezione);
            proiezione.setOraInizio(oraInizio);
            proiezione.setNumeroSala(numeroSala);
            proiezione.setFilmId(filmId);
            proiezione.setDeleted(false);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return proiezione;
    }

    @Override
    public void update(Proiezione proiezione) throws ConflictingProjectionException {
        PreparedStatement ps;
        try {
            // Check for conflicting projections
            if (isConflictingProjection(proiezione.getDataProiezione(), proiezione.getOraInizio(),
                    proiezione.getNumeroSala(), proiezione.getIdProiezione())) {
                throw new ConflictingProjectionException("Esiste già una proiezione in conflitto per questa sala e orario.");
            }

            String sql = "UPDATE proiezione SET data_proiezione = ?, ora_inizio = ?, numero_sala = ?, " +
                    "film_id = ?, deleted = ? WHERE id_proiezione = ?";

            ps = connection.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(proiezione.getDataProiezione().getTime()));
            ps.setTime(2, proiezione.getOraInizio());
            ps.setInt(3, proiezione.getNumeroSala());
            ps.setInt(4, proiezione.getFilmId());
            ps.setString(5, proiezione.isDeleted() ? "y" : "n");
            ps.setInt(6, proiezione.getIdProiezione());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Proiezione proiezione) {
        PreparedStatement ps;
        try {
            String sql = "UPDATE proiezione SET deleted = 'y' WHERE id_proiezione = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, proiezione.getIdProiezione());
            ps.executeUpdate();
            proiezione.setDeleted(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Proiezione FindById(Integer idProiezione) {
        PreparedStatement ps;
        Proiezione proiezione = null;

        try {
            String sql = "SELECT * FROM proiezione WHERE id_proiezione = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, idProiezione);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                proiezione = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return proiezione;
    }

    @Override
    public List<Proiezione> FindByFilmId(Integer filmId) {
        PreparedStatement ps;
        List<Proiezione> proiezioni = new ArrayList<>();

        try {
            String sql = "SELECT * FROM proiezione WHERE film_id = ? AND deleted = 'n' ORDER BY data_proiezione, ora_inizio";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, filmId);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                proiezioni.add(read(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return proiezioni;
    }

    private Proiezione read(ResultSet rs) throws SQLException {
        Proiezione proiezione = new Proiezione();
        proiezione.setIdProiezione(rs.getInt("id_proiezione"));
        proiezione.setDataProiezione(rs.getDate("data_proiezione"));
        proiezione.setOraInizio(rs.getTime("ora_inizio"));
        proiezione.setNumeroSala(rs.getInt("numero_sala"));
        proiezione.setFilmId(rs.getInt("film_id"));
        proiezione.setDeleted(rs.getString("deleted").equals("y"));
        return proiezione;
    }

    private boolean isConflictingProjection(Date dataProiezione, Time oraInizio, Integer numeroSala, Integer excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM proiezione p JOIN film f ON p.film_id = f.id_film " +
                "WHERE p.numero_sala = ? AND p.data_proiezione = ? AND p.deleted = 'n' " +
                "AND ((p.ora_inizio <= ? AND ADDTIME(p.ora_inizio, SEC_TO_TIME(f.durata_minuti * 60)) > ?) " +
                "OR (p.ora_inizio < ADDTIME(?, SEC_TO_TIME(? * 60)) AND p.ora_inizio >= ?))";

        if (excludeId != null) {
            sql += " AND p.id_proiezione != ?";
        }

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, numeroSala);
        ps.setDate(2, new java.sql.Date(dataProiezione.getTime()));
        ps.setTime(3, oraInizio);
        ps.setTime(4, oraInizio);
        ps.setTime(5, oraInizio);
        ps.setInt(6, 120); // Assuming a standard 2-hour duration, adjust as needed
        ps.setTime(7, oraInizio);

        if (excludeId != null) {
            ps.setInt(8, excludeId);
        }

        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public static class ConflictingProjectionException extends Exception {
        public ConflictingProjectionException(String message) {
            super(message);
        }
    }
}