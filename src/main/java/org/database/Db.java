package org.database;

import org.Model.exception.DaoException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Db {


    public static Connection getConnectionDb() {
        try {
            Properties props = getPropriedade();
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    private static Properties getPropriedade() {
        try (FileInputStream fis = new FileInputStream("jdbc.properties")) {
            Properties props = new Properties();
            props.load(fis);
            return props;
        } catch (IOException e) {
            throw new DaoException(e.getMessage());
        }
    }


}
