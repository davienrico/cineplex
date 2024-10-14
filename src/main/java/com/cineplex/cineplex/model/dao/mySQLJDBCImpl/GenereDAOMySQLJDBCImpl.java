package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.GenereDAO;
import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Genere;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenereDAOMySQLJDBCImpl implements GenereDAO {

    private Connection connection;

    public GenereDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Genere create(int idGenere, String nomeGenere, List<Film> films) throws DuplicateGenreException {
        PreparedStatement ps;
        Genere genere = new Genere();
        try {
            // Check if the genre name already exists
            if (isGenreNameExist(nomeGenere)) {
                throw new DuplicateGenreException("Il genere '" + nomeGenere + "' esiste già.");
            }

            String sql = "INSERT INTO genere (nome_genere) VALUES (?)";

            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nomeGenere);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenere = rs.getInt(1);
            }

            genere.setIdGenere(idGenere);
            genere.setNomeGenere(nomeGenere);
            genere.setDeleted(false);

            // Insert film associations
            if (films != null && !films.isEmpty()) {
                insertFilmAssociations(idGenere, films);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return genere;
    }

    @Override
    public void update(Genere genere) throws DuplicateGenreException {
        PreparedStatement ps;
        try {
            // Check if the new genre name already exists (excluding this genre)
            if (isGenreNameExistExcludingThis(genere.getNomeGenere(), genere.getIdGenere())) {
                throw new DuplicateGenreException("Il genere '" + genere.getNomeGenere() + "' esiste già.");
            }

            String sql = "UPDATE genere SET nome_genere = ?, deleted = ? WHERE id_genere = ?";

            ps = connection.prepareStatement(sql);
            ps.setString(1, genere.getNomeGenere());
            ps.setString(2, genere.isDeleted() ? "y" : "n");
            ps.setInt(3, genere.getIdGenere());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Genere genere) {
        PreparedStatement ps;
        try {
            String sql = "UPDATE genere SET deleted = 'y' WHERE id_genere = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, genere.getIdGenere());
            ps.executeUpdate();
            genere.setDeleted(true);

            // Also delete associations in film_genere table
            deleteFilmAssociations(genere.getIdGenere());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Genere FindById(Integer idGenere) {
        PreparedStatement ps;
        Genere genere = null;

        try {
            String sql = "SELECT * FROM genere WHERE id_genere = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, idGenere);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                genere = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return genere;
    }

    @Override
    public List<Genere> FindByFilm(List<Film> films) {
        PreparedStatement ps;
        List<Genere> generi = new ArrayList<>();

        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT DISTINCT g.* FROM genere g " +
                            "JOIN film_genere fg ON g.id_genere = fg.genere_id " +
                            "WHERE g.deleted = 'n' AND fg.film_id IN ("
            );
            for (int i = 0; i < films.size(); i++) {
                sql.append(i > 0 ? "," : "").append("?");
            }
            sql.append(")");

            ps = connection.prepareStatement(sql.toString());
            int paramIndex = 1;
            for (Film film : films) {
                ps.setInt(paramIndex++, film.getIdFilm());
            }

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                generi.add(read(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return generi;
    }

    @Override
    public Genere FindByNomeGenere(String nomeGenere) {
        PreparedStatement ps;
        Genere genere = null;

        try {
            String sql = "SELECT * FROM genere WHERE nome_genere = ? AND deleted = 'n'";

            ps = connection.prepareStatement(sql);
            ps.setString(1, nomeGenere);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                genere = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return genere;
    }

    private Genere read(ResultSet rs) throws SQLException {
        Genere genere = new Genere();
        genere.setIdGenere(rs.getInt("id_genere"));
        genere.setNomeGenere(rs.getString("nome_genere"));
        genere.setDeleted(rs.getString("deleted").equals("y"));
        return genere;
    }

    private boolean isGenreNameExist(String nomeGenere) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM genere WHERE nome_genere = ? AND deleted = 'n'");
        ps.setString(1, nomeGenere);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private boolean isGenreNameExistExcludingThis(String nomeGenere, int genreId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM genere WHERE nome_genere = ? AND id_genere != ? AND deleted = 'n'");
        ps.setString(1, nomeGenere);
        ps.setInt(2, genreId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private void insertFilmAssociations(int genreId, List<Film> films) throws SQLException {
        String sql = "INSERT INTO film_genere (film_id, genere_id) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        for (Film film : films) {
            ps.setInt(1, film.getIdFilm());
            ps.setInt(2, genreId);
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private void deleteFilmAssociations(int genreId) throws SQLException {
        String sql = "DELETE FROM film_genere WHERE genere_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, genreId);
        ps.executeUpdate();
    }

    public static class DuplicateGenreException extends Exception {
        public DuplicateGenreException(String message) {
            super(message);
        }
    }
}