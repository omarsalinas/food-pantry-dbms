package org.foodpantry.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.foodpantry.db.DBConnection;

public class AdminUI {

	private static String insertStmt = null;
	private static Statement stmt = null;

	
	public AdminUI(){
		createAndShowGUI();
	}
	/**
	 * Create a pane that allows users to be added
	 */
	private static JPanel adminAddUserPane() {
		JPanel addUserPane = new JPanel();

		// Add username label
		JLabel usernameLabel = new JLabel();
		usernameLabel.setText("Username:");
		addUserPane.add(usernameLabel);
		
		// Add user name text field
		final JTextField usernameTextField = new JTextField(10);
		usernameTextField.setToolTipText("username to add");
		addUserPane.add(usernameTextField);

		// Add password label
		JLabel passwordLabel = new JLabel();
		passwordLabel.setText("Password:");
		addUserPane.add(passwordLabel);
				
		// Add password field
		final JPasswordField passwordField = new JPasswordField(10);
		passwordField.setToolTipText("password field");
		addUserPane.add(passwordField);
		
		// Add administrator label
		JLabel adminLabel = new JLabel();
		adminLabel.setText("Admin?:");
		addUserPane.add(adminLabel);
		
		final JCheckBox adminBox = new JCheckBox();
		adminBox.setSelected(false);
		adminBox.setToolTipText("administrator?");
		addUserPane.add(adminBox);
		
		final JButton addUserButton = new JButton("Add User");
		addUserButton.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get connection to database from MainUI
				DBConnection conn = MainUI.dbConnection;
				int insert = 0;
				
				// Check to make sure the username and password filed are not empty
				if ( usernameTextField.getText() != null &&
						usernameTextField.getText().trim().length() != 0 &&
						passwordField.getPassword() != null 
						&& passwordField.getPassword().length != 0) {
											
					// Check the admin boolean to see if the user should be an admin or not
					// and build the query accordingly
					if(adminBox.isSelected()){
						insertStmt = "INSERT INTO Pantry_Security VALUES ('" + usernameTextField.getText()
								+ "', '" + passwordField.getText().toString() 
								+"', true)";						
					}
					else{
						insertStmt = "INSERT INTO Pantry_Security VALUES ('" + usernameTextField.getText() 
								+ "', '" + passwordField.getText().toString() 
								+"', false)";	
					}
				
					// Debugging printing of username and password
					System.out.println("User:"+ usernameTextField.getText());
					System.out.println("Password:" + passwordField.getPassword().toString());
									
					// Proceed only if connection was successful
					if (conn.Success) {
						// execute delete
						try{
							stmt = conn.getDBConnection().createStatement();
							insert = stmt.executeUpdate(insertStmt);
						}
						catch (SQLException s){
							  System.out.println("Adding user FAILED" + s.toString());
						}

						if (insert > 0 ) {
							System.out.println("User was added.");
							JOptionPane.showMessageDialog(
								    addUserButton, "Username was added",
								    "Added User",
								    JOptionPane.INFORMATION_MESSAGE);
						} else {
							System.err.println("User was not added.");
						}
					}
					usernameTextField.setText("");
					passwordField.setText("");
					adminBox.setSelected(false);
				}
				else {
					//display error say a user name and password must be entered to add
					System.err.println("Username and Password cannot be blank");
					JOptionPane.showMessageDialog(
						    addUserButton, "Username and Password must be entered to add a new user.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			
			}
		});
		addUserPane.add(addUserButton);

		return addUserPane;
	}

	/**
	 * Create a pane that allows users to be deleted
	 */
	private static JPanel adminDeleteUserPane() {
		JPanel deleteUserPane = new JPanel();
		
		// Delete username Label
		JLabel deleteLabel = new JLabel();
		deleteLabel.setText("Username to delete:");
		deleteUserPane.add(deleteLabel);
		
		// Delete user name text field
		final JTextField usernameTextField = new JTextField(10);
		usernameTextField.setToolTipText("username to delete");
		deleteUserPane.add(usernameTextField);

		// Delete  user button
		final JButton deleteUserButton = new JButton("Delete User");
		deleteUserPane.add(deleteUserButton);
		deleteUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Get connection to database from MainUI
				DBConnection conn = MainUI.dbConnection;
				int delete = 0;

				if (usernameTextField.getText() != null) {
					String deleteStmt = "DELETE FROM Pantry_Security WHERE User_Name='"
							+ usernameTextField.getText() + "'";
					
					// proceed only if connection was successful
					if (conn.Success) {
						// execute delete
						try {
							stmt = conn.getDBConnection().createStatement();
							delete = stmt.executeUpdate(deleteStmt);
						} catch (SQLException s) {
							  System.out.println("Deleting user FAILED" + s.toString());
						}
						if (delete > 0) {
							System.out.println("User was deleted.");
							JOptionPane.showMessageDialog(
									deleteUserButton, "Username was deleted",
								    "Deleted User",
								    JOptionPane.INFORMATION_MESSAGE);
						} else {
							System.err.println("User not deleted.");
						}
					}
					usernameTextField.setText("");
				}

				else {
					// display error say a user name must be entered to delete
					System.err.println("Username cannot be blank");
					JOptionPane.showMessageDialog(
						    deleteUserButton, "Username must be entered to delete a new user.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return deleteUserPane;
	}

	/**
	 * Create a pane that allows users to be modified
	 */
	private static JPanel adminModifyUserPane() {
		JPanel modifyUserPane = new JPanel();
		
		// Query to populate the table
		String getUsers = "SELECT * FROM Pantry_Security";

		// Get connection to database from MainUI
		DBConnection conn = MainUI.dbConnection;

		//Result set to store data
		ResultSet rs = null;
		ResultSetTable rst = null;
		
		// proceed only if connection was successful
		if (conn.Success) {
			// execute delete
			try {
				stmt = conn.getDBConnection().createStatement();
				rs = stmt.executeQuery(getUsers);
				rst = new ResultSetTable(rs);
			} catch (SQLException s) {
				  System.out.println("Error getting admin users" + s.toString());
			}
		}
		
		// Add administrator label
		JLabel modifyLabel = new JLabel();
		modifyLabel.setText("Modify User:");
		modifyUserPane.add(modifyLabel);
		
		modifyUserPane.add(new JScrollPane(rst));
		
		return modifyUserPane;
	}
	
	public static void main(String[] args) {
		// Invoke the thread with 
		// Create and show the main GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AdminUI.createAndShowGUI();
			}
		});
	}

	protected static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Administrative Services");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addButtonsToPane(frame.getContentPane());
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

 	private static void addButtonsToPane(final Container pane) {
		//set parent pane to spring layout
		SpringLayout layout = new SpringLayout();
		pane.setLayout(layout);

		// Add  user button
		final JButton addButton = new JButton("Add User");
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Add the add user pane
				// Create and set up the window.
				JFrame frame = new JFrame("Adding User");
				frame.add(AdminUI.adminAddUserPane());
				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
		pane.add(addButton);
		
		// Delete  user button
		final JButton deleteButton = new JButton("Delete User");
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Add the delete user pane
				// Create and set up the window.
				JFrame frame = new JFrame("Deleting User");
				frame.add(AdminUI.adminDeleteUserPane());
				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
		pane.add(deleteButton);

		// Modify  user button
		final JButton modifyButton = new JButton("Modify User");
		modifyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Add the delete user pane
				// Create and set up the window.
				JFrame frame = new JFrame("Modifying User");
				frame.add(AdminUI.adminModifyUserPane());
				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
		pane.add(modifyButton);
			
		// Standard padding for the elements
		int padding = 5;
		// constraints on the placement of the status bar
		layout.putConstraint(SpringLayout.WEST, addButton,
				 padding,SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, addButton,
				 padding,SpringLayout.NORTH, pane);
		
		layout.putConstraint(SpringLayout.WEST, deleteButton,
				 padding,SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, deleteButton,
				 padding,SpringLayout.SOUTH, addButton);
		
		layout.putConstraint(SpringLayout.WEST, modifyButton,
				 padding,SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, modifyButton,
				 padding,SpringLayout.SOUTH, deleteButton);
		
		
		// Constrain the pane to the internal elements
		layout.putConstraint(SpringLayout.SOUTH, pane,
				 padding, SpringLayout.SOUTH, modifyButton);
		layout.putConstraint(SpringLayout.EAST, pane,
				 padding, SpringLayout.EAST, deleteButton);
	}

	
}
