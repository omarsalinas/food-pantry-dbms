package org.foodpantry.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public Connection connection = null;
	
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
		} catch (Exception e) {
			System.err.println("Failed to Connect to Datatbase");
			e.printStackTrace();
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
	 * Main Method used to locally test a database connection
	 * @param args
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