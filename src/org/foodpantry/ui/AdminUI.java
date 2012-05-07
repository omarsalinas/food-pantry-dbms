package org.foodpantry.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.foodpantry.db.DBConnection;

public class AdminUI {

	/**
	 * Create a pane that allows users to be added
	 */
	private static JPanel adminAddUserPane() {
		JPanel addUserPane = new JPanel();

		final JTextField usernameTextField = new JTextField(10);
		addUserPane.add(usernameTextField);

		final JPasswordField passwordField = new JPasswordField(10);
		addUserPane.add(passwordField);

		JButton addUserButton = new JButton("Add User");
		addUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (usernameTextField.getText() != null && passwordField.getPassword() != null) {
					String insertStmt = "INSERT INTO Pantry_Security VALUES ('" + usernameTextField.getText() + "', '" + passwordField.getPassword().toString() +"')";
					
					// create connection to database
					DBConnection conn = new DBConnection();

					// proceed only if connection was successful
					if (conn.Success) {
						// execute delete
						boolean insert = conn.executeUpdate(insertStmt);
						if (insert == true) {
							System.out.println("User was added.");
						} else {
							System.err.println("User was not added.");
						}
					}
					// close connection
					conn.closeConnection();
				}
				else {
					// TODO - display error say a user name and password must be entered to add
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

		final JTextField usernameTextField = new JTextField(10);
		deleteUserPane.add(usernameTextField);

		JButton deleteUserButton = new JButton("Delete User");
		deleteUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (usernameTextField.getText() != null) {
					String deleteStmt = "DELETE FROM Pantry_Security WHERE User_Name='"
							+ usernameTextField.getText() + "'";
					
					// create connection to database
					DBConnection conn = new DBConnection();

					// proceed only if connection was successful
					if (conn.Success) {
						// execute delete
						boolean delete = conn.executeUpdate(deleteStmt);
						if (delete == true) {
							System.out.println("User was deleted.");
						} else {
							System.err.println("User not deleted.");
						}
					}
					// close connection
					conn.closeConnection();
				}

				else {
					// TODO - display error say a user name must be entered to delete
				}
			}
		});
		deleteUserPane.add(deleteUserButton);

		return deleteUserPane;
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
		//Create and set up the window.
		JFrame frame = new JFrame("Administrative Services");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Set up the content pane.
		addComponentsToPane(frame.getContentPane());
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private static void addComponentsToPane(Container pane) {
		//set parent pane to springlayout
		SpringLayout layout = new SpringLayout();
		pane.setLayout(layout);
		// Add pane for admin actions
		JPanel addUser = AdminUI.adminAddUserPane();
		JLabel addLabel = new JLabel();
		addLabel.setText("Add User:");
		pane.add(addUser);
		pane.add(addLabel);

		JPanel deleteUser = AdminUI.adminDeleteUserPane();
		JLabel deleteLabel = new JLabel();
		deleteLabel.setText("Delete User:");
		pane.add(deleteUser);
		pane.add(deleteLabel);
		
		// Standard padding for the elements
		int padding = 5;
		// constraints on the placement of the status bar
		layout.putConstraint(SpringLayout.WEST, addLabel,
				 padding,SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, addLabel,
				 padding,SpringLayout.NORTH, pane);
		
		layout.putConstraint(SpringLayout.WEST, addUser,
				 padding,SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, addUser,
				 padding,SpringLayout.SOUTH, addLabel);

		layout.putConstraint(SpringLayout.WEST, deleteLabel,
				 padding,SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, deleteLabel,
				 padding,SpringLayout.SOUTH, addUser);
		
		layout.putConstraint(SpringLayout.WEST, deleteUser,
				 padding,SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, deleteUser,
				 padding, SpringLayout.SOUTH, deleteLabel);
	}
}
