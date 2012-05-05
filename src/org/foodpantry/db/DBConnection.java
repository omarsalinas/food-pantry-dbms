package org.foodpantry.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnection {

	public Connection connection = null;
	public boolean Success = false; //used to check if conn was good
	//public ResultSet results = null;//var to hold result from query
	public Object results;
	
	/**
	 * Constructor for DBConnection which create a connection to the database
	 */
	public DBConnection(){
		String url = "jdbc:mysql://mysql1007.ixwebhosting.com:3306/";
		String dbName = "C242386_foodpantry";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "C242386_group"; 
		String password = "Cosc578";
		
		
		try {
			System.out.println("Connecting to Database");
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url+dbName,userName,password);
			System.out.println("Connected to mysql on Server");
			Success = true;
			
		} catch (Exception e) {
			System.err.println("Failed to Connect to Datatbase");
			e.printStackTrace();
			Success = false;
		}
	}
	
	/**
	 * closeConnection method used to close the connection to the database
	 */
	public void closeConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println("Failed to close connection to Datatbase");
			e.printStackTrace();
		}
	}
	
	/**
	 * Function to execute a statement against db which returns no data
	 * set. Returns true if update was successful.*/
	public boolean executeUpdate(String statement)
	{
		//*TO DO
		return true;
	}
	
	/**
	 * Function to execute a statement against db which returns a dataset
	 * @throws SQLException 
	 */
	public void executeStatement(String statement) throws SQLException
	{
		
		//create statement object to send statement to db
		Statement query =  connection.createStatement();
		
		//run query against db
		query.executeQuery(statement);
		
		//get result set
		results = query.getResultSet();
		
		//release resources
		//query.close();
		
	}
	
	/**
	  Main Method used to locally test a database connection
	  @param args
	 */
	public static void main(String[] args) {
		Connection conn = null;
		String url = "jdbc:mysql://mysql1007.ixwebhosting.com:3306/";
		String dbName = "C242386_foodpantry";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "C242386_group"; 
		String password = "Cosc578";

		try {
			System.out.println("Connecting to Database");
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName,userName,password);
			System.out.println("Connected to mysql on Server");
			conn.close();
			System.out.println("Disconnected from Database");
		} catch (Exception e) {
			System.err.println("Failed to Connect to Datatbase");
			e.printStackTrace();
		}
	}
}