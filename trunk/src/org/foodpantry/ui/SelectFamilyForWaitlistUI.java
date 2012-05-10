package org.foodpantry.ui;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

/**
 * Window that lets the user choose someone to add to the waitlist.
 * TODO needs to enforce that family credentials are good before adding
 */
public class SelectFamilyForWaitlistUI extends JFrame implements ActionListener {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -1534413015745702353L;
	
	/**
	 * Title of JFrame
	 */
	private String title = "Add a family to the waitlist.";
	
	/**
	 * Family IDs to be edited
	 */
	ArrayList<Integer> familyIDs = new ArrayList<Integer>();

	FamilyTableModel model;

	/**
	 * Constructs and adds all needed elements.  Also, packs the frame.
	 * Does not, however, set the frame to visible.
	 * @throws HeadlessException
	 */
	public SelectFamilyForWaitlistUI() throws HeadlessException {		
		// Get the container to make following code a little easier to read
		Container pane = this.getContentPane();
		
		// Set JFrame Configuration
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout layout = new SpringLayout();
		pane.setLayout(layout);
		
		this.model = new FamilyTableModel();
		JTable table = new JTable(this.model);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(
				scrollPane.getBorder(), "Families",
				TitledBorder.LEFT, TitledBorder.TOP));
		
		pane.add(scrollPane);
		
		JPanel buttonPane = this.createButtonPane();
		pane.add(buttonPane);
		
		/*
		 * pane layout
		 * +------------
		 * | buttonPane
		 * |------------
		 * | scrollPane
		 * +------------
		 */
		int padding = 5;
		// Edit family constraints
		layout.putConstraint(SpringLayout.WEST, buttonPane,
				 padding,
				 SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, buttonPane,
				 padding, 
				 SpringLayout.NORTH, pane);
		// Scrollpane constraints
		layout.putConstraint(SpringLayout.WEST, scrollPane,
				 padding,
				 SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, scrollPane,
				 padding,
				 SpringLayout.SOUTH, buttonPane);
		// pane constraints
		layout.putConstraint(SpringLayout.SOUTH, pane,
				 padding, 
				 SpringLayout.SOUTH, scrollPane);
		layout.putConstraint(SpringLayout.EAST, pane, 
				 padding, 
				 SpringLayout.EAST, scrollPane);
		
		this.pack();
	}

	/**
	 * Create a pane that displays the following:
	 * 	-"Add Family to List" button
	 *  -"Edit Family Information" button
	 */
	private JPanel createButtonPane() {
		JPanel pane = new JPanel();
		
		JButton addSelectionBtn = new JButton("Add Selection");
		addSelectionBtn.addActionListener(this);
		addSelectionBtn.setActionCommand("Add Selection");
		addSelectionBtn.setMnemonic(KeyEvent.VK_A);
		pane.add(addSelectionBtn);

		JButton addNewFamilyBtn = new JButton("Create Family");
		//addNewFamilyBtn.addActionListener(new openWindow());
		addNewFamilyBtn.addActionListener(this);
		addNewFamilyBtn.setActionCommand("Add New Family");
		addNewFamilyBtn.setMnemonic(KeyEvent.VK_C);
		pane.add(addNewFamilyBtn);
		
		JButton editFamilyBtn = new JButton("Edit Family");
		editFamilyBtn.addActionListener(this); 
		editFamilyBtn.setActionCommand("Edit Family");
		editFamilyBtn.setMnemonic(KeyEvent.VK_E);
		pane.add(editFamilyBtn);
	
		return pane; 
	}
	
	/**
	 * Finds all the selected rows and adds those families to the waitlist.
	 */
	private void addFamiliesToWaitlist() {
		/*
		 * The SQL statement should read something like:
		 * INSERT INTO Visit_Pantry
		 * VALUES (today, fam#, time, #inLine),
		 * VALUES (today, fam#, time, #inLine);
		 */
		String sql = "INSERT INTO Visit_Pantry VALUES ";
		
		// Create Current Date String
		Date todayDate = new Date();
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		String currDate = dateFormatter.format(todayDate);
		
		// Create Current Time String
		DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
		String currTime = timeFormatter.format(todayDate);
		
		// Get the current total of families on the waiting list, and set that
		// as the first family's number in line, increasing for each additional
		// family
		int numberInLine = MainUI.model.getRowCount();
		
		// boolean that should indicate if something is selected, and thus
		// the sql statement should be executed
		boolean selectionExists = false;
		
		// Iterate over the list of families and put the selection(s) on the waitlist
		for (int i=0 ; i<this.model.getRowCount(); i++) {
			// if a row was selected, append to the sql statement
			if ((Boolean)this.model.getValueAt(i, 0) == true ) {
				// once a selection is found, all following will need a comma
				// prepended
				if (selectionExists) {
					sql += ",";
				} else {
					selectionExists = true;
				}
				
				String values = "(" + "'" + currDate + "'" + "," + 
								(Integer)this.model.getValueAt(i, 1) + "," +
								"'" + currTime + "'" + "," + numberInLine++ + ") ";
				sql += values;
			}
		}
		
		// close the sql statement
		sql += ";";
		
		// try to execute the sql statement
		try {
			if (selectionExists) {
				MainUI.dbConnection.connection.prepareStatement(sql).execute();
			}
		} catch (SQLException e) {
			System.out.println("SelectFamilyForWaitlistUI: Failed to execute query");
			System.out.println(sql);
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add New Family")) {
			JFrame window = new AddNewFamilyUI();
			window.pack();
			window.setVisible(true);
		} else if (e.getActionCommand().equals("Edit Family")) {
			JFrame window = new EditFamilyUI();
			window.pack();
			window.setVisible(true);
		} else if (e.getActionCommand().equals("Add Selection")) {
			this.addFamiliesToWaitlist();
			this.dispose();
		}
	}

}
