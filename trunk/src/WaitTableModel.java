import javax.swing.table.AbstractTableModel;

/**
 * The table model to represent the family and waitlist information to be 
 * displayed on the waitlist
 */
public class WaitTableModel extends AbstractTableModel {

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Order","Name","# in group",
									"Veg","Frig","Pantry"};
	private Object[][] data = {
			{"1","Smith","4",true,true,true},
			{"2","Johnson","4",true,false,true},
			{"3","Jones","4",true,true,false},
			{"4","Simpson","4",false,true,false},
			{"5","Washington","4",true,false,true},
			};
	
	/**
	 * Return the number of columns.
	 */
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * Return the number of rows.
	 */
	@Override
	public int getRowCount() {
		return data.length;
	}

	/**
	 * Return the name of the column
	 */
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	/**
	 * Return the value at a specific row and column.
	 */
	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
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
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}
