package com.cdero.gamerbot.sql;

//import statements
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cdero.gamerbot.GamerBotApplication;

/**
 * 
 * @author		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class SQLConnection {
	
	/**
	 * Stored connection to the SQL server.
	 */
	private static Connection sqlConnection;
	
	/**
	 * Check if the SQL server is connected or not.
	 */
	private static Boolean connected = false;
	
	/**
	 * Logger for the SQLConnection class.
	 */
	private final static Logger log = LogManager.getLogger(GamerBotApplication.class.getName());
	
	/**
	 * Connect the Gamer Bot Application to the MySQL/MariaDB server.
	 * 
	 */
	public static void connectToSQL() {
		
		try {
			
			String dbURL = "jdbc:mysql://db.cdero.com:3306/gamerbot";
			String dbUser = "gamerbot";
			String dbPassword = "L6Bw4NdEhkxuZGtX";
			
			sqlConnection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			sqlConnection.setAutoCommit(false);
			
			connected = true;
			
		} catch (SQLException se) {

			log.fatal("Unable to connect to MySQL/MariaDB server, shutting down...");
			System.exit(1);
			
		}
		
		log.info("Gamer Bot Application successfully connected to MySQL/MariaDB...");
		
	}
	
	/**
	 * Getter for the MySQL/MariaDB connection.
	 * 
	 * @return	Connection	Returns the connection to the MySQL/MariaDB server.
	 */
	public static Connection getSQL() {
		
		if (connected) {
			
			return sqlConnection;
			
		}else {
			
			log.info("Not connected to MySQL/MariaDB, connecting now...");
			
			connectToSQL();
			
			return sqlConnection;
			
		}
		
	}
	
	/**
	 * Get the status of the MySQL/MariaDB server
	 * 
	 * @return	Boolean	Either true or false depending if the SQL server is still connected.
	 */
	public static Boolean getSQLStatus() {
		
		Boolean status = false;
		
		try {
			
			log.info("Checking if the connection to MySQL/MariaDB is still valid...");
			
			status = sqlConnection.isValid(3);
			
		} catch (SQLException e) {

			log.fatal("Unable to contact the MySQL/MariaDB server" +
			"\n Data will not be saved! Please check the connection to the MySQL/MariaDB server!");
			
			return false;
			
		} finally {
			
			log.info("Connection to MySQL/MariaDB is valid...");
			
		}
		
		return status;
		
	}
	
	/**
	 * Query the MySQL/MariaDB server with passed data.
	 * 
	 * @param	query	The SQL query that will be run in the MySQL/MariaDB server.
	 * @return	ResultSet	The data that is returned from the query that was run.
	 */
	public static ResultSet query(String query) {
		
		Statement sqlStatement = null;
		
		ResultSet data = null;
		
		try {
			
			sqlStatement = sqlConnection.createStatement();
			
			data = sqlStatement.executeQuery(query);
			
			sqlConnection.commit();
			
		} catch (SQLException se) {
			
			log.fatal("Unable to contact the MySQL/MariaDB server with:" + 
			"\n" + query +
			"\nData will not be saved! Please check the connection to the MySQL/MariaDB server!");
			
		}
		
		return data;
		
	}
	
	public static void batchQuery(Statement sqlStatement) {
		
		try {
			
			sqlStatement.executeBatch();
			sqlConnection.commit();
			
		} catch (SQLException se) {
			
			log.fatal("There was an error executing a batch query!" + 
			"\nData will not be saved! Please check the connection to the MySQL/MariaDB server!");
			log.error(se);
			
		}
		
	}

}
