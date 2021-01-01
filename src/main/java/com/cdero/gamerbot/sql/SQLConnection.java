package com.cdero.gamerbot.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.cdero.gamerbot.GamerBotApplication;

public class SQLConnection {
	
	/**
	 * Stored connection to the SQL server
	 */
	private static Connection sqlConnection;
	
	/**
	 * Check if the SQL server is connected or not
	 */
	private static Boolean connected = false;
	
	/**
	 * Logger for this class
	 */
	private final static Logger log = Logger.getLogger(GamerBotApplication.class.getPackage().getName());
	
	/**
	 * Connect the application to the MySQL/MariaDB server
	 * 
	 * @author 		Christopher DeRoche 
	 * @version		1.0
	 * @since		1.0
	 * 
	 * @return		void
	 */
	public static void connectToSQL() {
		
		try {
			
			String dbURL = "jdbc:mysql://db.cdero.com/gamer_bot";
			String dbUser = "gamerbot";
			String dbPassword = "L6Bw4NdEhkxuZGtX";
			
			sqlConnection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			
			connected = true;
			
		} catch (SQLException se) {

			log.severe("Unable to connect to MySQL/MariaDB server, shutting down...");
			System.exit(1);
			
		}
		
	}
	
	/**
	 * Getter for the MySQL/MariaDB connection
	 * 
	 * @author 		Christopher DeRoche 
	 * @version		1.0
	 * @since		1.0
	 * 
	 * @return		Connection	Returns the connection to the MySQL/MariaDB server
	 */
	public static Connection getSQL() {
		
		if (connected) {
			
			return sqlConnection;
			
		}else {
			
			connectToSQL();
			
			return sqlConnection;
			
		}
		
	}
	
	/**
	 * Query the MySQL/MariaDB server with passed data.
	 * 
	 * @author 		Christopher DeRoche 
	 * @version		1.0
	 * @since		1.0
	 * 
	 * @param		query	The SQL query that will be run in the MySQL/MariaDB server.
	 * @return		ResultSet	The data that is returned from the query that was run.
	 */
	public static ResultSet query(String query) {
		
		Statement sqlStatement = null;
		
		ResultSet data = null;
		
		try {
			
			sqlStatement = sqlConnection.createStatement();
			
			data = sqlStatement.executeQuery(query);
			
		} catch (SQLException se) {
			
			log.severe("Unable to contact the MySQL/MariaDB server with: " + query + "\n Please check the connection or status of the server");
			
		}
		
		return data;
		
	}

}
