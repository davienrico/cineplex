package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.DAOFactory;
import com.cineplex.cineplex.model.dao.FilmDAO;
import com.cineplex.cineplex.model.mo.Film;
import com.cineplex.cineplex.model.mo.Genere;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilmDAOMySQLJDBCImpl implements FilmDAO {

    private Connection connection;

    public FilmDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Film create(Integer idFilm, String titolo, Date dataInizioDisponibilita, Date dataFineDisponibilita,
                       String percorsoLocandina, Date dataPubblicazione, Integer durataMinuti, String descrizione,
                       String linkTrailerYT, String regista, List<Genere> generi) throws DuplicateTitleException {

        PreparedStatement ps;
        Film film = new Film();
        try {
            // Check if the title already exists
            if (isTitleExist(titolo)) {
                throw new DuplicateTitleException("Il titolo del film '" + titolo + "' esiste già.");
            }

            String sql = "INSERT INTO film (titolo, data_inizio_disponibilita, data_fine_disponibilita, " +
                    "percorso_locandina, data_pubblicazione, durata_minuti, descrizione, " +
                    "link_trailer_yt, regista) VALUES (?,?,?,?,?,?,?,?,?)";

            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setString(i++, titolo);
            ps.setDate(i++, new java.sql.Date(dataInizioDisponibilita.getTime()));
            ps.setDate(i++, new java.sql.Date(dataFineDisponibilita.getTime()));
            ps.setString(i++, percorsoLocandina);
            ps.setDate(i++, new java.sql.Date(dataPubblicazione.getTime()));
            ps.setInt(i++, durataMinuti);
            ps.setString(i++, descrizione);
            ps.setString(i++, linkTrailerYT);
            ps.setString(i++, regista);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idFilm = rs.getInt(1);
            }

            film.setIdFilm(idFilm);
            film.setTitolo(titolo);
            film.setDataInizioDisponibilita(dataInizioDisponibilita);
            film.setDataFineDisponibilita(dataFineDisponibilita);
            film.setPercorsoLocandina(percorsoLocandina);
            film.setDataPubblicazione(dataPubblicazione);
            film.setDurataMinuti(durataMinuti);
            film.setDescrizione(descrizione);
            film.setLinkTrailerYt(linkTrailerYT);
            film.setRegista(regista);
            film.setDeleted(false);
            film.setGeneri(generi);

            // Insert genres
            insertGenres(idFilm, generi);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return film;
    }

    @Override
    public void update(Film film) throws DuplicateTitleException {
        PreparedStatement ps;
        try {
            // Check if the new title already exists (excluding this film)
            if (isTitleExistExcludingThis(film.getTitolo(), film.getIdFilm())) {
                throw new DuplicateTitleException("Il titolo del film '" + film.getTitolo() + "' esiste già.");
            }

            String sql = "UPDATE film SET titolo = ?, data_inizio_disponibilita = ?, " +
                    "data_fine_disponibilita = ?, percorso_locandina = ?, data_pubblicazione = ?, " +
                    "durata_minuti = ?, descrizione = ?, link_trailer_yt = ?, regista = ?, " +
                    "deleted = ? WHERE id_film = ?";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, film.getTitolo());
            ps.setDate(i++, new java.sql.Date(film.getDataInizioDisponibilita().getTime()));
            ps.setDate(i++, new java.sql.Date(film.getDataFineDisponibilita().getTime()));
            ps.setString(i++, film.getPercorsoLocandina());
            ps.setDate(i++, new java.sql.Date(film.getDataPubblicazione().getTime()));
            ps.setInt(i++, film.getDurataMinuti());
            ps.setString(i++, film.getDescrizione());
            ps.setString(i++, film.getLinkTrailerYt());
            ps.setString(i++, film.getRegista());
            ps.setString(i++, film.getDeleted() ? "y" : "n");
            ps.setInt(i++, film.getIdFilm());

            ps.executeUpdate();

            // Update genres
            updateGenres(film.getIdFilm(), film.getGeneri());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Film film) {
        PreparedStatement ps;
        try {
            String sql = "UPDATE film SET deleted = 'y' WHERE id_film = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, film.getIdFilm());
            ps.executeUpdate();
            film.setDeleted(true);

            // Also delete from film_genere table
            deleteGenres(film.getIdFilm());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Film> findAll() throws Exception {
        List<Film> films = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM film WHERE deleted = 'n'";
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Film film = read(rs);
                film.setGeneri(getGenresForFilm(film.getIdFilm()));
                films.add(film);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }

        return films;
    }

    @Override
    public Film FindById(Integer idFilm) {
        PreparedStatement ps;
        Film film = null;

        try {
            String sql = "SELECT * FROM film WHERE id_film = ?";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, idFilm);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                film = read(resultSet);
                film.setGeneri(getGenresForFilm(idFilm));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return film;
    }

    @Override
    public List<Film> FindByTitolo(String query) throws Exception {
        List<Film> films = new ArrayList<>();
        try {
            String sql = "SELECT * FROM film WHERE titolo LIKE ? AND deleted = 'n'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + query + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Film film = read(resultSet);
                film.setGeneri(getGenresForFilm(film.getIdFilm()));
                films.add(film);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }

    @Override
    public List<Film> FindByGenere(List<Genere> generi) {
        PreparedStatement ps;
        List<Film> films = new ArrayList<>();

        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT DISTINCT f.* FROM film f " +
                            "JOIN film_genere fg ON f.id_film = fg.film_id " +
                            "WHERE f.deleted = 'n' AND fg.genere_id IN ("
            );
            for (int i = 0; i < generi.size(); i++) {
                sql.append(i > 0 ? "," : "").append("?");
            }
            sql.append(")");

            ps = connection.prepareStatement(sql.toString());
            int paramIndex = 1;
            for (Genere genere : generi) {
                ps.setInt(paramIndex++, genere.getIdGenere());
            }

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Film film = read(resultSet);
                film.setGeneri(getGenresForFilm(film.getIdFilm()));
                films.add(film);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return films;
    }

    private Film read(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setIdFilm(rs.getInt("id_film"));
        film.setTitolo(rs.getString("titolo"));
        film.setDataInizioDisponibilita(rs.getDate("data_inizio_disponibilita"));
        film.setDataFineDisponibilita(rs.getDate("data_fine_disponibilita"));
        String percorsoLocandina = rs.getString("percorso_locandina");
        film.setPercorsoLocandina(percorsoLocandina);
        film.setDataPubblicazione(rs.getDate("data_pubblicazione"));
        film.setDurataMinuti(rs.getInt("durata_minuti"));
        film.setDescrizione(rs.getString("descrizione"));
        film.setLinkTrailerYt(rs.getString("link_trailer_yt"));
        film.setRegista(rs.getString("regista"));
        film.setDeleted(rs.getString("deleted").equals("y"));
        return film;
    }

    private boolean isTitleExist(String titolo) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM film WHERE titolo = ? AND deleted = 'n'");
        ps.setString(1, titolo);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private boolean isTitleExistExcludingThis(String titolo, int filmId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM film WHERE titolo = ? AND id_film != ? AND deleted = 'n'");
        ps.setString(1, titolo);
        ps.setInt(2, filmId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private void insertGenres(int filmId, List<Genere> generi) throws SQLException {
        String sql = "INSERT INTO film_genere (film_id, genere_id) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        for (Genere genere : generi) {
            ps.setInt(1, filmId);
            ps.setInt(2, genere.getIdGenere());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private void updateGenres(int filmId, List<Genere> generi) throws SQLException {
        // First, delete all existing genres for this film
        deleteGenres(filmId);
        // Then, insert the new genres
        insertGenres(filmId, generi);
    }

    private void deleteGenres(int filmId) throws SQLException {
        String sql = "DELETE FROM film_genere WHERE film_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, filmId);
        ps.executeUpdate();
    }

    private List<Genere> getGenresForFilm(int filmId) throws SQLException {
        List<Genere> generi = new ArrayList<>();
        String sql = "SELECT g.* FROM genere g JOIN film_genere fg ON g.id_genere = fg.genere_id WHERE fg.film_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, filmId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Genere genere = new Genere();
            genere.setIdGenere(rs.getInt("id_genere"));
            genere.setNomeGenere(rs.getString("nome_genere"));
            genere.setDeleted(rs.getString("deleted").equals("y"));
            generi.add(genere);
        }
        return generi;
    }

    public static class DuplicateTitleException extends Exception {
        public DuplicateTitleException(String message) {
            super(message);
        }
    }

    @Override
    public List<Film> searchFilms(String title, Date date) throws Exception {
        List<Film> films = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder("SELECT DISTINCT f.* FROM film f ");
            sql.append("LEFT JOIN proiezione p ON f.id_film = p.film_id ");
            sql.append("WHERE f.deleted = 'n' ");

            List<Object> params = new ArrayList<>();

            if (title != null && !title.trim().isEmpty()) {
                sql.append("AND f.titolo LIKE ? ");
                params.add("%" + title + "%");
            }

            if (date != null) {
                sql.append("AND p.data_proiezione = ? ");
                params.add(new java.sql.Date(date.getTime()));
            }

            sql.append("ORDER BY f.titolo");

            pstmt = connection.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Film film = read(rs);
                film.setGeneri(getGenresForFilm(film.getIdFilm()));
                films.add(film);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }

        return films;
    }

    @Override
    public List<Film> findFeaturedFilms() throws Exception {
        List<Film> featuredFilms = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT DISTINCT f.*, MIN(p.data_proiezione) as next_projection " +
                    "FROM film f " +
                    "JOIN proiezione p ON f.id_film = p.film_id " +
                    "WHERE f.deleted = 'n' " +
                    "AND p.data_proiezione >= CURDATE() " +
                    "GROUP BY f.id_film " +
                    "ORDER BY next_projection ASC ";

            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Film film = read(rs);
                film.setGeneri(getGenresForFilm(film.getIdFilm()));
                featuredFilms.add(film);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }

        return featuredFilms;
    }
}