package org.foodpantry.ui;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

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
							"Phone",
							"Address"};
	
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
		String sql = 
		"SELECT f.Family_Number, f.Last_Name, f.Primary_Name, f.No_Children, f.No_Adults, ifnull(fp.Phone_Number,0)," +
		" ifnull(fa.House_Number,0), ifnull(fa.Street,'n/a'), ifnull(fa.City,'n/a')" +
					 "FROM Family f left outer join Family_Address fa " +
					 "on f.Family_number = fa.Family_Number left outer join Family_Phone_Number fp" +
					 " on f.Family_Number = fp.Family_Number;";
		
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
				this.data[row][0] = new Boolean(false);
				this.data[row][1] = result.getInt(1);
				this.data[row][2] = result.getString(2);
				this.data[row][3] = result.getString(3);
				this.data[row][4] = result.getInt(4); 
				this.data[row][5] = result.getInt(5);
				this.data[row][6] = result.getString(6);
				this.data[row][7] = result.getInt(7) + " " + result.getString(8) + " " + result.getString(9);				
			}
		} catch (SQLException e) {
			System.out.println("Family Table Model SQL Query Failed");
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
			return true;
		}
		
		return false; 
	}
	
	/**
	 * Overrides parent function.  Necessary for boolean value to render properly
	 */
	@Override
	public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		this.data[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
	}
}
