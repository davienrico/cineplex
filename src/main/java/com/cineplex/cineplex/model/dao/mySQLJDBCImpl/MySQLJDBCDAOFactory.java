package com.cineplex.cineplex.model.dao.mySQLJDBCImpl;

import com.cineplex.cineplex.model.dao.*;
import com.cineplex.cineplex.services.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class MySQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private Connection connection;

    public MySQLJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AbbonamentoDAO getAbbonamentoDAO() {
        return null;
    }

    @Override
    public BigliettoDAO getBigliettoDAO() {
        return null;
    }

    @Override
    public FilmDAO getFilmDAO() {
        return new FilmDAOMySQLJDBCImpl(connection);
    }

    @Override
    public GenereDAO getGenereDAO() {
        return null;
    }

    @Override
    public ProiezioneDAO getProiezioneDAO() {
        return null;
    }

    @Override
    public RecensioneDAO getRecensioneDAO() {
        return null;
    }

    @Override
    public TipoAbbonamentoDAO getTipoAbbonamentoDAO() {
        return null;
    }

    @Override
    public UtenteDAO getUtenteDAO() {
            return new UtenteDAOMySQLJDBCImpl(connection);
    }

}