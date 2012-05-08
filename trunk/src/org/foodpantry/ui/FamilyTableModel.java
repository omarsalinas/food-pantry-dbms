package org.foodpantry.ui;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

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
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	Object[][] contents;
	String[] columnNames;
	Class[] columnClasses;
	Connection conn = (Connection) MainUI.dbConnection.connection;
	String tableName = "Family";

	public FamilyTableModel () throws SQLException {
		super();
	}

	protected void getTableContents () throws SQLException {

		// get metadata: what columns exist and what
		// types (classes) are they?
		DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();
		System.out.println ("got meta = " + meta);
		ResultSet results =
				meta.getColumns (null, null, tableName, null) ;
		System.out.println ("got column results");
		ArrayList<String> colNamesList = new ArrayList<String>();
		ArrayList<Class> colClassesList = new ArrayList<Class>();
		while (results.next()) {
			colNamesList.add (results.getString ("COLUMN_NAME")); 
			System.out.println ("name: " + 
					results.getString ("COLUMN_NAME"));
			int dbType = results.getInt ("DATA_TYPE");
			switch (dbType) {
			case Types.INTEGER:
				colClassesList.add (Integer.class); break; 
			case Types.FLOAT:
				colClassesList.add (Float.class); break; 
			case Types.DOUBLE: 
			case Types.REAL:
				colClassesList.add (Double.class); break; 
			case Types.DATE: 
			case Types.TIME: 
			case Types.TIMESTAMP:
				colClassesList.add (java.sql.Date.class); break; 
			default:
				colClassesList.add (String.class); break; 
			}; 
			System.out.println ("type: " +
					results.getInt ("DATA_TYPE"));
		}
		columnNames = new String [colNamesList.size()];
		colNamesList.toArray (columnNames);
		columnClasses = new Class [colClassesList.size()];
		colClassesList.toArray (columnClasses);

		// get all data from table and put into
		// contents array
		Statement statement =
				(Statement) conn.createStatement ();
		results = statement.executeQuery ("SELECT * FROM " +
				tableName);
		ArrayList<Object[]> rowList = new ArrayList<Object[]>();
		while (results.next()) {
			ArrayList<Object> cellList = new ArrayList<Object>(); 
			for (int i = 0; i<columnClasses.length; i++) { 
				Object cellValue = null;


				if (columnClasses[i] == String.class) 
					cellValue = results.getString (columnNames[i]); 
				else if (columnClasses[i] == Integer.class) 
					cellValue = new Integer ( 
							results.getInt (columnNames[i])); 
				else if (columnClasses[i] == Float.class) 
					cellValue = new Float ( 
							results.getInt (columnNames[i])); 
				else if (columnClasses[i] == Double.class) 
					cellValue = new Double ( 
							results.getDouble (columnNames[i]));
				else if (columnClasses[i] == java.sql.Date.class) 
					cellValue = results.getDate (columnNames[i]); 
				else 
					System.out.println ("Can't assign " + 
							columnNames[i]);
				cellList.add (cellValue);
			}// for
			Object[] cells = cellList.toArray();
			rowList.add (cells);

		} // while
		// finally create contents two-dim array
		contents = new Object[rowList.size()] [];
		for (int i=0; i<contents.length; i++)

			contents[i] = rowList.get (i);
		System.out.println ("Created model with " +
				contents.length + " rows");

		// close stuff
		results.close();
		statement.close();

	}

	@Override
	public int getColumnCount() {
		if (contents.length == 0)
			return 0;
		else
			return contents[0].length;
	}

	public Object getValueAt (int row, int column) {
		return contents [row][column];
	}

	// overrides methods for which AbstractTableModel
	// has trivial implementations

	public Class getColumnClass (int col) {
		return columnClasses [col];
	}

	public String getColumnName (int col) { 
		return columnNames [col]; 
	}

	@Override
	public int getRowCount() {
		return contents.length;
	}

}
