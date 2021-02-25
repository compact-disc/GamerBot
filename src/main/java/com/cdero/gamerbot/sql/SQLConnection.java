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
 * @author Christopher DeRoche
 * @version 1.0
 * @since 1.0
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
	 * @throws SQLException	When there is an error connecting the MariaDB connector to the database.
	 */
	public static void connectToSQL() throws SQLException {

		String dbURL = "jdbc:mysql://db.cdero.com:3306/gamerbot";
		String dbUser = "gamerbot";
		String dbPassword = "L6Bw4NdEhkxuZGtX";

		sqlConnection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		sqlConnection.setAutoCommit(false);

		connected = true;

	}

	/**
	 * Getter for the MySQL/MariaDB connection.
	 * 
	 * @return Connection Returns the connection to the MySQL/MariaDB server.
	 * @throws SQLException	If there is an error connecting to the SQL server. This is if the method is called and somehow it is not connected.
	 */
	public static Connection getSQL() throws SQLException {

		if (connected) {

			return sqlConnection;

		} else {

			log.info("Not connected to MySQL/MariaDB, connecting now...");

			connectToSQL();

			return sqlConnection;

		}

	}

	/**
	 * Get the status of the MySQL/MariaDB server
	 * 
	 * @return Boolean Either true or false depending if the SQL server is still connected.
	 * @throws SQLException	When there is an error checking the status of an SQL Connector.
	 */
	public static Boolean getSQLStatus() throws SQLException {

		Boolean status = false;
			
		status = sqlConnection.isValid(3);

		return status;

	}

	/**
	 * 
	 * 
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet query(String query) throws SQLException {

		Statement sqlStatement = null;

		ResultSet data = null;

		sqlStatement = sqlConnection.createStatement();

		data = sqlStatement.executeQuery(query);

		sqlConnection.commit();

		return data;

	}

	/**
	 * 
	 * 
	 * @param sqlStatement
	 * @throws SQLException
	 */
	public static void batchQuery(Statement sqlStatement) throws SQLException {

		sqlStatement.executeBatch();
		sqlConnection.commit();

	}

}
