package com.cineplex.cineplex.services.logservice;

import com.cineplex.cineplex.services.config.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.*;

public class LogService {
    private static Logger applicationLogger;

    private LogService() {
    }

    public static Logger getApplicationLogger() {
        if (applicationLogger == null) {
            applicationLogger = Logger.getLogger(Configuration.GLOBAL_LOGGER_NAME);
            setupLogger();
        }
        return applicationLogger;
    }

    private static void setupLogger() {
        try {
            // Ensure the directory exists
            Files.createDirectories(Paths.get(Configuration.GLOBAL_LOGGER_FILE).getParent());

            Handler fileHandler = new FileHandler(Configuration.GLOBAL_LOGGER_FILE, true);
            SimpleFormatter formatterTxt = new SimpleFormatter();
            fileHandler.setFormatter(formatterTxt);

            applicationLogger.addHandler(fileHandler);
            applicationLogger.setLevel(Configuration.GLOBAL_LOGGER_LEVEL);
            applicationLogger.setUseParentHandlers(false);

            applicationLogger.log(Level.CONFIG, "Logger: {0} created.", applicationLogger.getName());
        } catch (IOException e) {
            System.err.println("Error setting up logger: " + e.getMessage());
            e.printStackTrace();
        }
    }
}