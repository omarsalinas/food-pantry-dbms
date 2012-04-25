package org.foodpantry.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	public static void main(String[] args) {
		Connection conn = null;
		String url = "jdbc:mysql://triton.towson.edu:3306/marbau1/";
		String dbName = "";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "marbau1"; 
		String password = "";

		try {
			System.out.println("Connecting to Triton");
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName,userName,password);
			System.out.println("Connected to mysql on Triton");
			conn.close();
			System.out.println("Disconnected from Triton");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}