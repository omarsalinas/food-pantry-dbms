package org.foodpantry.ui;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
	
	Connection conn;
	
	Database(Connection connection){
		conn = connection;
	}
	
	/**
	 * If check is true enter station into Station_Record, if false, then delete record from Station_Record;
	 * @param familyName
	 * @param Station
	 * @param date
	 * @param check
	 */
	public void updateStationRecord(int familyNumber, String station, Date date, Boolean check){

		if(check){
			try {
				PreparedStatement insertStatement = null;
				String insertSQL = "INSERT INTO Station_Record (S_Name, Family_Number, Date_Visisted) VALUES (?, ?, ?)";
				insertStatement = conn.prepareStatement(insertSQL);
				insertStatement.setString(1, station);
				insertStatement.setInt(2, familyNumber);
				insertStatement.setDate(3, date);
				insertStatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			PreparedStatement deleteRowStatement = null;
			String deleteRowSQL = "DELETE * FROM Station_Record WHERE S_Name = ? AND Family_Number = ? AND Date_Visited = ?";
			try {
				deleteRowStatement = conn.prepareStatement(deleteRowSQL);
				deleteRowStatement.setString(1, station);
				deleteRowStatement.setInt(2, familyNumber);
				deleteRowStatement.setDate(3, date);
				deleteRowStatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param familyNumber
	 * @return String LastName
	 * @throws SQLException
	 */
	public String getFamilyLastName(int familyNumber) throws SQLException{
		String LastName = null;
		PreparedStatement selectStatement;
		ResultSet resultSet;
		
		String querySQL = "SELECT Last_Name FROM Family WHERE Family_Number = ?";
		selectStatement = conn.prepareStatement(querySQL);
		selectStatement.setInt(1, familyNumber);
		resultSet = selectStatement.executeQuery();
		if(resultSet.next()){
			LastName = resultSet.getString("Last_Name");
		}
		return LastName;
	}
	
	/**
	 * @param familyNumber
	 * @return number of people in the family
	 * @throws SQLException
	 */
	public int getNoInFamily(int familyNumber) throws SQLException{
		int noChildren = 0;
		int noAdults = 0;
		PreparedStatement selectStatement;
		ResultSet resultSet;
		
		String querySQL = "SELECT No_Children, No_Adults FROM Family WHERE Family_Number = ?";
		selectStatement = conn.prepareStatement(querySQL);
		selectStatement.setInt(1, familyNumber);
		resultSet = selectStatement.executeQuery();
		if(resultSet.next()){
			noChildren = resultSet.getInt("No_Children");
			noAdults = resultSet.getInt("No_Adults");
		}
		return (noChildren + noAdults);
	}
	
	/**
	 * @param date
	 * @param familyNumber
	 * @return List of stations
	 * @throws SQLException
	 */
	public List<String> getStationsFamilyVisit(Date date, int familyNumber) throws SQLException{
		List<String> stations = new ArrayList<String>();
		PreparedStatement selectStatement;
		ResultSet resultSet;
		
		String querySQL = "SELECT S_Name FROM Station_Record WHERE Date_Visited = ? AND Family_Number = ?";
		selectStatement = conn.prepareStatement(querySQL);
		selectStatement.setDate(1, date);
		selectStatement.setInt(2, familyNumber);
		resultSet = selectStatement.executeQuery();
		while(resultSet.next()){
			stations.add(resultSet.getString("S_Name"));
		}
		return stations;
	}
	
	/**
	 * Returns a list that includes both 
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> getListFromVisitPanty(Date date) throws SQLException{
		List<Integer> familyNumbers = new ArrayList<Integer>();
		PreparedStatement selectStatement;
		ResultSet resultSet;
		
		String querySQL = "SELECT Family_Number, Number FROM Visit_Pantry where Date_Visited = ? order by Number";
		selectStatement = conn.prepareStatement(querySQL);
		selectStatement.setDate(1, date);
		resultSet = selectStatement.executeQuery();
		while(resultSet.next()){
			familyNumbers.add(resultSet.getInt("Family_Number"));
			familyNumbers.add(resultSet.getInt("Number"));
		}
		return familyNumbers;
	}
		
	/**
	 * Retrieves all to the stations used for a certain date.
	 * @param date
	 * @return List of stations
	 * @throws SQLException
	 */
	public List<String> getStations(Date date) throws SQLException{
		
		List<String> stations = new ArrayList<String>();
		PreparedStatement selectStatement;
		ResultSet resultSet;
		
		String querySQL = "SELECT S_Name FROM Station_Record where Date_Visited = ?";
		selectStatement = conn.prepareStatement(querySQL);
		selectStatement.setDate(1, date);
		resultSet = selectStatement.executeQuery();
		while(resultSet.next()){
			stations.add(resultSet.getString("S_Name"));
		}
		return stations;
	}
	
	/**
	 * TODO find a way to input the date from the user
	 * @return date
	 * @throws SQLException
	 */
	public Date getDate() throws SQLException{
		Date date;
		PreparedStatement selectStatement;
		ResultSet resultSet;
		
		String querySQL = "SELECT Date_Visited FROM Station_Record";
		selectStatement = conn.prepareStatement(querySQL);
		resultSet = selectStatement.executeQuery();
		if(resultSet.next()){
			date = resultSet.getDate("Date_Visited");
			System.out.println(date);
			return date;
		}
		return null;
	}
}
