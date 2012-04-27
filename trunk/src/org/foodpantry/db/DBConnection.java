package org.foodpantry.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

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