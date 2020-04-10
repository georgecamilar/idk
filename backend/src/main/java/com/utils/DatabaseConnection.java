package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseConnection {
    private Properties props;
    private static final Logger logger = LogManager.getLogger();
    private Connection connection = null;

    public DatabaseConnection(Properties props) {
        this.props = props;
        logger.info("[INFO] Attached properties file");
    }

    private Connection obtainConnection() {
        logger.traceEntry();
        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");
        logger.info("Connecting to url{} using:\n*username - {}\n*password - {}", url, username, password);

        Connection dbCon = null;
        try {
            if (username != null && password != null) {
                dbCon = DriverManager.getConnection(url, username, password);
            } else {
                dbCon = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            System.out.printf("[ERROR] Cannot getting connection to db: %s", e.getMessage());
        }
        return dbCon;
    }

    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = this.obtainConnection();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " -- " + e.getSQLState());
        }
        return this.connection;
    }


}
