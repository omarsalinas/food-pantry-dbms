package org.foodpantry.ui;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class AdminTableModel extends AbstractTableModel {

	/**
	 * Columns should be:
	 * -User_Name
	 * -Password
	 * -Admin
	 */
	String[] columnNames = {"User_Name",
							"Password",
							"Admin"};
	
	Object[][] data;
	
	public AdminTableModel() {
		super();
		
		/*
		 * Query the database and fill the data
		 */
		String sql = "SELECT * FROM Pantry_Security";
		
		ResultSet result = null;
		try {
			result =  MainUI.dbConnection.connection.prepareStatement(sql).executeQuery();

			// initialize the data array
			result.last();
			this.data = new Object[result.getRow()][getColumnCount()];
			result.beforeFirst();
			
			// read in the data
			while (result.next()) {
				int row = result.getRow() - 1;
				this.data[row][0] = result.getString(1);
				this.data[row][1] = result.getString(2);
				this.data[row][2] = result.getInt(3);
			}
		} catch (SQLException e) {
			System.out.println("AdminUI Table Model SQL Query Failed");
			e.printStackTrace();
		}
	}
	
	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
		return this.data.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.data[rowIndex][columnIndex];
	}
	
	@Override
	public String getColumnName(int col) {
		return this.columnNames[col].toString();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if ( columnIndex == 0 ) {
			return false;
		}
		
		return true; 
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		this.data[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
	}
}
