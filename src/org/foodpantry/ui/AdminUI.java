package org.foodpantry.ui;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AdminUI {

	
	/**
	 * Create a pane that allows users to be added
	 */
	private static JPanel adminAddUserPane() {
		JPanel addUserPane = new JPanel();

	    JTextField usernameTextField = new JTextField();
	    addUserPane.add(usernameTextField);
		
		JPasswordField passwordField = new JPasswordField(10);
		addUserPane.add(passwordField);
		
		JButton addUserButton = new JButton("Add User");
		addUserButton.addActionListener();
		addUserPane.add(addUserButton);
		
		return addUserPane; 
	}
	
	/**
	 * Create a pane that allows users to be deleted
	 */
	private static JPanel adminDeleteUserPane() {
		JPanel deleteUserPane = new JPanel();

	    JTextField usernameTextField = new JTextField();
	    deleteUserPane.add(usernameTextField);
		
		JButton deleteUserButton = new JButton("Delete User");
		deleteUserButton.addActionListener();
		deleteUserPane.add(deleteUserButton);
		
		return deleteUserPane; 
	}
	
	
}
