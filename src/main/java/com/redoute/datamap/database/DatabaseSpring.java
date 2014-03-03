package com.redoute.datamap.database;

import com.redoute.datamap.log.Logger;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Database class, allow to get Connections defined on glassfish.
 *
 * @author bcivel
 */
@Repository
public class DatabaseSpring {

    /**
     * Object autowired by Spring, linked to glassfish for getting connection.
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Create connection.
     * <p/>
     * If the connection doesn't exist, one will be created through the
     * DataSource object. If the connection exists, is reused without the
     * necessity of creating another. Then update the status.
     *
     * @return Connection Object with the connection created by DataSource
     *         object.
     */
    public Connection connect() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException exception) {
            Logger.log(DatabaseSpring.class.getName(), Level.ERROR, "Cannot connect to datasource jdbc/datamap : " + exception.toString());
        }

        return null;
    }

    public Connection connect(final String connection) {
        try {
            InitialContext ic = new InitialContext();
            Logger.log(DatabaseSpring.class.getName(), Level.INFO, "connecting to jdbc/" + connection);
            return ((DataSource) ic.lookup("jdbc/" + connection)).getConnection();
        } catch (SQLException ex) {
            Logger.log(DatabaseSpring.class.getName(), Level.ERROR, ex.toString());
        } catch (NamingException ex) {
            Logger.log(DatabaseSpring.class.getName(), Level.FATAL, ex.toString());
        }
        return null;
    }
}
