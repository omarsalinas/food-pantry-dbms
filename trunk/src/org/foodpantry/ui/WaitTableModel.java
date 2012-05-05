package org.foodpantry.ui;
import java.sql.Date;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The table model to represent the family and waitlist information to be 
 * displayed on the waitlist
 */
public class WaitTableModel extends AbstractTableModel {

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Date for the list
	 **/
	private Date listDate;

	private List<String> columnNames = new ArrayList<String>();

	private List<ListData> listData = new ArrayList<ListData>();
	
	class ListData{
		int order;
		String name;
		int numberInGroup;
		List<Boolean> stations;
		
		ListData(int order, String name, int number, List<Boolean> stations){
			this.order = order;
			this.name = name;
			this.numberInGroup = number;
			this.stations = stations;
		}
		
		private Object getColumn(int col){
			switch(col){
			case 0:
				return this.order;
			case 1:
				return this.name;
			case 2:
				return this.numberInGroup;
			default:
				if(this.stations != null && this.stations.toArray().length >= (col - 3))
					return this.stations.toArray()[(col - 3)];
			}
			return false;
		}
		
		private void setColumn(int col, Object value){
			switch(col){
			case 0:
				this.order = (Integer) value;
				break;
			case 1:
				this.name = (String) value;
				break;
			case 2:
				this.numberInGroup = (Integer) value;
				break;
			default:
				if(this.stations != null && this.stations.toArray().length >= (col - 3))
					this.stations.toArray()[(col - 3)] = (Boolean) value;
			} 
		}		
	}
	
	WaitTableModel(Database database){
		//I am having trouble finding a good way to enter the 
		//date into the database. -Lynn
		refeshList(database);	
	}

	public void refeshList(Database database){
		
		List<String> stationNames = new ArrayList<String>();
		
		//Titles of the columns in the list
		this.columnNames.clear();
		columnNames.add("Order");
		columnNames.add("Name");
		columnNames.add("# in group");
		try {
			listDate = database.getDate(); //TODO !!!!!!!!!! find a different way to select the date!!!!!!
			stationNames = database.getStations(this.listDate);
			if(stationNames != null){
				Iterator<String> stationNameIterator = stationNames.iterator();
				while(stationNameIterator.hasNext()){
					columnNames.add(stationNameIterator.next());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//What are we going to do about errors?
			e.printStackTrace();
		}
		
		//Data populating this list
		try {
			List<Integer> numbers = new ArrayList<Integer>();
			numbers = database.getListFromVisitPanty(listDate);
			if(numbers != null){
				Iterator<Integer> numbersIterator = numbers.iterator();
				while(numbersIterator.hasNext()){
					int familyNumber = numbersIterator.next();
					int number = numbersIterator.next();
					String LastName = database.getFamilyLastName(familyNumber);
					
					List<Boolean> stationCheck = new ArrayList<Boolean>();
					List<String> station = new ArrayList<String>();
					station = database.getStationsFamilyVisit(listDate, familyNumber);
					if(stationNames != null && station != null){
						Iterator<String> stationNameIterator = stationNames.iterator();
						while(stationNameIterator.hasNext()){
							boolean check = false;
							String stationName = stationNameIterator.next();
							Iterator<String> stationIterator = station.iterator();
							while(stationIterator.hasNext()){
								if(stationName.compareTo(stationIterator.next()) == 0)
									check = true;
							}
							stationCheck.add(check);
						}
					}
					
					ListData data = new ListData(familyNumber, LastName, number, stationCheck);
					listData.add(data);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Return the number of columns.
	 */
	@Override
	public int getColumnCount() {
		if(columnNames != null)
			return columnNames.size();
		else
			return 0;
	}

	/**
	 * Return the number of rows.
	 */
	@Override
	public int getRowCount() {
		if(listData != null)
			return listData.size();
		else 
			return 0;
	}

	/**
	 * Return the name of the column
	 */
	@Override
	public String getColumnName(int col) {
		Object[] names = columnNames.toArray();
		return (String) names[col];
	}

	/**
	 * Return the value at a specific row and column.
	 */
	@Override
	public Object getValueAt(int row, int col) {
		ListData data = (ListData) listData.toArray()[row];
		return data.getColumn(col) ;
	}

	/**
	 * Return the class type of the column data.
	 * Necessary to let the checkboxes display instead of the boolean values
	 */
	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/**
	 * Checks to see if the cell is editable.
	 * Allow the last three columns, which should be the checkboxes,
	 * to be editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		if(col < this.getColumnCount()-3 ) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Sets the value at the given location to the given value.
	 * Necessary for the editing of the checkboxes.
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		ListData data = (ListData) listData.toArray()[row];
		data.setColumn(col, value);
	}

	/**-
	 * Used by the TransferHandler to support reordering.
	 */
	public interface Reorderable {
		public void reorder(int fromIndex, int toIndex);
	}
}
