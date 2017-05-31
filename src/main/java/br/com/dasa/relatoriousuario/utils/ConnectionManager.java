package br.com.dasa.relatoriousuario.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import br.com.dasa.relatoriousuario.model.DataSource;

/**
 * 
 * @author t35615539828
 *
 */
public class ConnectionManager {

	static Logger logger = Logger.getLogger(ConnectionManager.class);
	
	private static Connection con;
	
	/**
	 * 
	 * @param dataSource
	 * @return
	 */
    public static Connection getConnection(DataSource dataSource) {
        try {
            Class.forName(dataSource.getDriverClassName());
            try {
                con = DriverManager.getConnection(dataSource.getUrl(), dataSource.getName(), dataSource.getPassword());
            } catch (SQLException ex) {
            	logger.error("Falha ao criar conexão com o banco.");
            }
        } catch (ClassNotFoundException ex) {
        	logger.error("Driver não encontrado.");
        }
        return con;
    }
}
