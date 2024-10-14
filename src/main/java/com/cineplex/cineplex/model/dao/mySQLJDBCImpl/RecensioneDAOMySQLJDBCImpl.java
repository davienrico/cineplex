package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.RecensioneDAO;
import com.cineplex.cineplex.model.mo.Recensione;
import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDAOMySQLJDBCImpl implements RecensioneDAO {

    private Connection connection;

    public RecensioneDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Recensione create(Integer idRecensione, Integer valutazione, String titoloRecensione, String testo,
                             Utente utente, Film film) throws DuplicateReviewException {
        PreparedStatement ps;
        Recensione recensione = new Recensione();
        try {
            // Check if the user has already reviewed this film
            if (hasUserReviewedFilm(utente.getIdUtente(), film.getIdFilm())) {
                throw new DuplicateReviewException("L'utente ha già recensito questo film.");
            }

            String sql = "INSERT INTO recensione (valutazione, titolo_recensione, testo, data_scrittura, utente_id, film_id) " +
                    "VALUES (?,?,?,NOW(),?,?)";

            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setInt(i++, valutazione);
            ps.setString(i++, titoloRecensione);
            ps.setString(i++, testo);
            ps.setInt(i++, utente.getIdUtente());
            ps.setInt(i++, film.getIdFilm());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idRecensione = rs.getInt(1);
            }

            // Fetch the newly created review to get the server-generated timestamp
            recensione = FindById(idRecensione);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return recensione;
    }

    @Override
    public void update(Recensione recensione) {
        PreparedStatement ps;
        try {
            String sql = "UPDATE recensione SET valutazione = ?, titolo_recensione = ?, testo = ?, " +
                    "data_scrittura = NOW(), deleted = ? WHERE id_recensione = ?";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, recensione.getValutazione());
            ps.setString(i++, recensione.getTitoloRecensione());
            ps.setString(i++, recensione.getTesto());
            ps.setString(i++, recensione.isDeleted() ? "y" : "n");
            ps.setInt(i++, recensione.getIdRecensione());

            ps.executeUpdate();

            // Update the recensione object with the new data_scrittura
            Recensione updatedRecensione = FindById(recensione.getIdRecensione());
            recensione.setDataScrittura(updatedRecensione.getDataScrittura());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Recensione recensione) {
        PreparedStatement ps;
        try {
            String sql = "UPDATE recensione SET deleted = 'y' WHERE id_recensione = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, recensione.getIdRecensione());
            ps.executeUpdate();
            recensione.setDeleted(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Recensione FindById(Integer idRecensione) {
        PreparedStatement ps;
        Recensione recensione = null;

        try {
            String sql = "SELECT * FROM recensione WHERE id_recensione = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, idRecensione);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                recensione = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return recensione;
    }

    @Override
    public List<Recensione> FindByUserId(Utente utente) {
        PreparedStatement ps;
        List<Recensione> recensioni = new ArrayList<>();

        try {
            String sql = "SELECT * FROM recensione WHERE utente_id = ? AND deleted = 'n' ORDER BY data_scrittura DESC";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, utente.getIdUtente());

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                recensioni.add(read(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return recensioni;
    }

    @Override
    public List<Recensione> FindByFilm(Film film) {
        PreparedStatement ps;
        List<Recensione> recensioni = new ArrayList<>();

        try {
            String sql = "SELECT * FROM recensione WHERE film_id = ? AND deleted = 'n' ORDER BY data_scrittura DESC";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, film.getIdFilm());

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                recensioni.add(read(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return recensioni;
    }

    private Recensione read(ResultSet rs) throws SQLException {
        Recensione recensione = new Recensione();
        recensione.setIdRecensione(rs.getInt("id_recensione"));
        recensione.setValutazione(rs.getInt("valutazione"));
        recensione.setTitoloRecensione(rs.getString("titolo_recensione"));
        recensione.setTesto(rs.getString("testo"));
        recensione.setDataScrittura(rs.getTimestamp("data_scrittura"));

        Utente utente = new Utente();
        utente.setIdUtente(rs.getInt("utente_id"));
        recensione.setUtente(utente);

        Film film = new Film();
        film.setIdFilm(rs.getInt("film_id"));
        recensione.setFilm(film);

        recensione.setDeleted(rs.getString("deleted").equals("y"));
        return recensione;
    }

    private boolean hasUserReviewedFilm(int userId, int filmId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM recensione WHERE utente_id = ? AND film_id = ? AND deleted = 'n'";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, filmId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public static class DuplicateReviewException extends Exception {
        public DuplicateReviewException(String message) {
            super(message);
        }
    }
}