package org.foodpantry.ui;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.Statement;

/**
 * The table model to represent the family information to be displayed when 
 * adding, editing, or searching a family
 * 
 * Utilizing tutorial at:
 * http://onjava.com/pub/a/onjava/excerpt/swinghks_hack24/index.html?page=2
 * 
 */
public class FamilyTableModel extends AbstractTableModel {

	/**
	 * Columns should be:
	 * -Family Number (Family table Primary key, ID)
	 * -Last Name (Family table)
	 * -Primary Name (Family table)
	 * -Number of Adults (Family table)
	 * -Number of Children (Family table)
	 * -Current Phone Number (Family_Phone_Number table)
	 * -Current Address (Family_Address)
	 */
	String[] columnNames = {"Select",
							"ID",
							"Last Name",
							"Primary",
							"# Adults",
							"# Children",
							"Current Phone",
							"Current Address"};
	
	Object[][] data;
	
	/**
	 * Generated Serial Version UID ... 
	 * Probably unnecessary for this application
	 */
	private static final long serialVersionUID = 362425068508364587L;

	public FamilyTableModel() {
		super();
		
		/*
		 * Query the database and fill the data
		 */
		String sql = "SELECT Family.Family_Number, Last_Name, Primary_Name, No_Children, No_Adults, Phone_Number, House_Number, Street, City " +
					 "FROM Family, Family_Address, Family_Phone_Number " +
					 "WHERE Family.Family_number = Family_Address.Family_Number " +
					 "AND Family.Family_number = Family_Phone_Number.Family_Number;";
		
		ResultSet result = null;
		try {
			result =  MainUI.dbConnection.connection.prepareStatement(sql).executeQuery();

			// initialize the data array
			result.last();
			data = new Object[result.getRow()][getColumnCount()];
			result.beforeFirst();
			
			// read in the data
			while (result.next()) {
				int row = result.getRow() - 1;
				data[row][0] = new Boolean(false);
				data[row][1] = result.getInt(1);
				data[row][2] = result.getString(2);
				data[row][3] = result.getString(3);
				data[row][4] = result.getInt(4); 
				data[row][5] = result.getInt(5);
				data[row][6] = result.getString(6);
				data[row][7] = result.getInt(7) + " " + result.getString(8) + " " + result.getString(9);				
			}
		} catch (SQLException e) {
			System.out.println("Family Table Model SQL Query Failed");
			e.printStackTrace();
		}
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		super.setValueAt(aValue, rowIndex, columnIndex);
	}
}
