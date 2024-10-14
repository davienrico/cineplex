package com.cineplex.cineplex.services.config;

import com.cineplex.cineplex.model.dao.DAOFactory;

import  java.util.Calendar;
import java.util.logging.Level;

public class Configuration {
    /* Database Configruation */
    public static final String DAO_IMPL= DAOFactory.MYSQLJDBCIMPL;
    public static final String DATABASE_DRIVER="com.mysql.cj.jdbc.Driver";
    public static final String SERVER_TIMEZONE= Calendar.getInstance().getTimeZone().getID();
    public static final String
            DATABASE_URL="jdbc:mysql://localhost/cineplex?user=root&password=GinoPinguino123&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone="+SERVER_TIMEZONE;

    /* Session Configuration */
    public static final String COOKIE_IMPL= DAOFactory.COOKIEIMPL;

    /* Logger Configuration */
    public static final String GLOBAL_LOGGER_NAME="cineplex";
    public static final String GLOBAL_LOGGER_FILE="/Users/bagyb/Documents/logs/cineplex_log.%g.%u.txt";
    public static final Level GLOBAL_LOGGER_LEVEL=Level.ALL;

}
