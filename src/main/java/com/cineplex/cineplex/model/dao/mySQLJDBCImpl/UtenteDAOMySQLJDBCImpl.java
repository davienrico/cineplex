package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.UtenteDAO;
import com.cineplex.cineplex.model.mo.Utente;
import com.cineplex.cineplex.model.dao.exceptions.UserExceptions.DuplicateEmailException;
import com.cineplex.cineplex.model.dao.exceptions.UserExceptions.DuplicateUsernameException;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtenteDAOMySQLJDBCImpl implements UtenteDAO {

    Connection conn;

    public UtenteDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Utente create(Integer idUtente, String nome, String cognome, String username, String email, String password, Boolean newsletter, String tipo)
            throws DuplicateUsernameException, DuplicateEmailException {

        PreparedStatement ps;
        Utente utente = new Utente();
        try {
            // Check if username already exists
            if (usernameExists(username)) {
                throw new DuplicateUsernameException("Username already exists");
            }

            // Check if email already exists
            if (emailExists(email)) {
                throw new DuplicateEmailException("Email already exists");
            }

            String sql = "INSERT INTO utente (nome, cognome, username, email, password, newsletter, tipo) VALUES (?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setString(i++, nome);
            ps.setString(i++, cognome);
            ps.setString(i++, username);
            ps.setString(i++, email);
            ps.setString(i++, hashPassword(password));
            ps.setString(i++, newsletter ? "y" : "n");
            ps.setString(i++, tipo);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idUtente = rs.getInt(1);
            }

            utente.setIdUtente(idUtente);
            utente.setNome(nome);
            utente.setCognome(cognome);
            utente.setUsername(username);
            utente.setEmail(email);
            utente.setPassword(password); // Note: We're not storing the hashed password in the object
            utente.setNewsletter(newsletter);
            utente.setTipo(tipo);
            utente.setDeleted(false);
            utente.setBanned(false);

        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return utente;
    }

    @Override
    public Utente authenticate(String username, String password) {
        PreparedStatement ps;
        Utente utente = null;

        try {
            String sql = "SELECT * FROM utente WHERE username = ? AND deleted = 'n'";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (verifyPassword(password, storedHash)) {
                    utente = read(rs);
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return utente;
    }

    private boolean usernameExists(String username) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM utente WHERE username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private boolean emailExists(String email) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM utente WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private boolean verifyPassword(String inputPassword, String storedHash) throws NoSuchAlgorithmException {
        String inputHash = hashPassword(inputPassword);
        return inputHash.equals(storedHash);
    }

    @Override
    public void update(Utente utente) throws DuplicateUsernameException, DuplicateEmailException {
        PreparedStatement ps;
        try {
            // Check if username already exists (excluding the current user)
            ps = conn.prepareStatement("SELECT COUNT(*) FROM utente WHERE username = ? AND id_utente != ?");
            ps.setString(1, utente.getUsername());
            ps.setInt(2, utente.getIdUtente());
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                throw new DuplicateUsernameException("Username already exists");
            }

            // Check if email already exists (excluding the current user)
            ps = conn.prepareStatement("SELECT COUNT(*) FROM utente WHERE email = ? AND id_utente != ?");
            ps.setString(1, utente.getEmail());
            ps.setInt(2, utente.getIdUtente());
            rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                throw new DuplicateEmailException("Email already exists");
            }

            String sql
                    = " UPDATE utente"
                    + " SET nome = ?,"
                    + " cognome = ?,"
                    + " username = ?,"
                    + " email = ?,"
                    + " password = ?,"
                    + " newsletter = ?,"
                    + " tipo = ?,"
                    + " banned = ?,"
                    + " deleted = ?"
                    + " WHERE"
                    + " id_utente = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, utente.getNome());
            ps.setString(i++, utente.getCognome());
            ps.setString(i++, utente.getUsername());
            ps.setString(i++, utente.getEmail());
            ps.setString(i++, utente.getPassword());
            ps.setString(i++, utente.isNewsletter() ? "y" : "n");
            ps.setString(i++, utente.getTipo());
            ps.setString(i++, utente.isBanned() ? "y" : "n");
            ps.setString(i++, utente.isDeleted() ? "y" : "n");
            ps.setInt(i++, utente.getIdUtente());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Utente utente) {
        PreparedStatement ps;
        try {
            String sql
                    = " UPDATE utente"
                    + " SET deleted = 'y'"
                    + " WHERE"
                    + " id_utente = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, utente.getIdUtente());
            ps.executeUpdate();
            utente.setDeleted(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void banUtente(int idUtente) {
        PreparedStatement ps;
        try {
            String sql
                    = " UPDATE utente"
                    + " SET banned = 'y'"
                    + " WHERE"
                    + " id_utente = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUtente);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Banning user failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unbanUtente(int idUtente) {
        PreparedStatement ps;
        try {
            String sql
                    = " UPDATE utente"
                    + " SET banned = 'n'"
                    + " WHERE"
                    + " id_utente = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUtente);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Unbanning user failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utente findLoggedUser() {
        throw new UnsupportedOperationException("Not supported");

    }

    @Override
    public Utente FindById(Integer idUtente) {
        PreparedStatement ps;
        Utente utente = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM utente"
                    + " WHERE"
                    + " id_utente = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUtente);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                utente = read(resultSet);
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return utente;
    }

    private Utente read(ResultSet rs) throws SQLException {
        Utente utente = new Utente();
        utente.setIdUtente(rs.getInt("id_utente"));
        utente.setNome(rs.getString("nome"));
        utente.setCognome(rs.getString("cognome"));
        utente.setUsername(rs.getString("username"));
        utente.setEmail(rs.getString("email"));
        // Note: We're not setting the password in the object for security reasons
        utente.setNewsletter(rs.getString("newsletter").equals("y"));
        utente.setTipo(rs.getString("tipo"));
        utente.setBanned(rs.getString("banned").equals("y"));
        utente.setDeleted(rs.getString("deleted").equals("y"));
        return utente;
    }

    @Override
    public Utente FindByUsername(String username) {
        PreparedStatement ps;
        Utente utente = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM utente"
                    + " WHERE"
                    + " username = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                utente = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return utente;
    }

    @Override
    public Utente FindByEmail(String email) {
        PreparedStatement ps;
        Utente utente = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM utente"
                    + " WHERE"
                    + " email = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                utente = read(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return utente;
    }


}
