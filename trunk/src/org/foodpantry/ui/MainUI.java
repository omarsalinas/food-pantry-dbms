package org.foodpantry.ui;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

/**
 * The main GUI.
 */
public class MainUI {
	
	/**
	 * Table to display the waitlist
	 */
//	private static JTable waitTable;
	
	/**
	 * Value to show if user is logged in.
	 * Initialized to false, for security.
	 */
	private static boolean loggedIn = false;
	
	/**
	 * Value to show if user is an administrator
	 * Initialized to false, for security.
	 */
	private static boolean administrator = false;
	
	/**
	 * Keep track of the user's name
	 */
	private static String username;
	
	/**
	 * Add all of the components to the pane to create the main GUI.
	 * @param pane
	 */
	public static void addComponentsToPane(Container pane) {
		// Add pane for user/admin/connection info
		pane.add(createStatusPane());
		
		// Create the model for the table
		WaitTableModel model = new WaitTableModel();
		// Initialize the family table and add the model to it
		JTable waitTable = new JTable(model);
		
		// Allow the table to support drag and drop
		// TODO setup the rest of the drag and drop functionality
		waitTable.setTransferHandler(new WaitListTransferHandler(waitTable));
		waitTable.setDragEnabled(true);
		waitTable.setDropMode(DropMode.INSERT_ROWS);
		
		// Create and setup a scrollpane, and add the table to it
		JScrollPane scrollPane = new JScrollPane(waitTable);
		scrollPane.setBorder(BorderFactory.createTitledBorder(
				scrollPane.getBorder(), "Waitlist",
				TitledBorder.LEFT, TitledBorder.TOP));
		
		// Add the scrollpane to the content pane
		pane.add(scrollPane);
	}
	
	/**
	 * Create a status pane that displays the following -check in test...
	 * 	-Welcome message for user
	 * 	-Connection status to server
	 *  -Administration button
	 */
	private static JPanel createStatusPane() {
		JPanel statusPane = new JPanel();
		JLabel user = new JLabel("Welcome, " + username + "!  ");
		
		// TODO add connection information
		JLabel connection = new JLabel("  The Connection is ...  ");
		
		statusPane.add(connection);
		statusPane.add(user);
		
		/*
		 * If the user is an administrator, give them an administration button.
		 * TODO give button functionality
		 * TODO create a panel to handle that functionality
		 */
		if (isAdministrator()) {
			JButton administrate = new JButton("Administration");
			statusPane.add(administrate);
		}
		
		return statusPane; 
	}
	
	/**
	 * Create and show the main GUI.  Should be implemented
	 * in a way to provide thread safety.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Food Pantry");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// TODO need a cleaner implementation
		//  Default username/password = bob/secret
		LoginDialog loginDlg = new LoginDialog(frame);
		loginDlg.setVisible(true);
		// if login succeeds, then add everything to the content pane
		// else shut 'er down!
		if(loginDlg.isSucceeded()) {
			loggedIn = true;
			username = loginDlg.getUsername();
			if(loginDlg.isAdministrator()) {
				administrator = true;
			}
			//Set up the content pane.
			addComponentsToPane(frame.getContentPane());
		} else {
			loggedIn = false;
			//Close down the JVM, since nothing is displayed
			System.exit(0);
		}

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		// Invoke the thread with 
		// Create and show the main GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainUI.createAndShowGUI();
			}
		});
	}

	public static boolean isLoggedIn() {
		return loggedIn;
	}

	public static boolean isAdministrator() {
		return administrator;
	}
}