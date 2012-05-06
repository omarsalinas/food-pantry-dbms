package org.foodpantry.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.foodpantry.db.DBConnection;

public class AdminUI {

	/**
	 * Create a pane that allows users to be added
	 */
	private static JPanel adminAddUserPane() {
		JPanel addUserPane = new JPanel();

		final JTextField usernameTextField = new JTextField();
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

		final JTextField usernameTextField = new JTextField();
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

}
