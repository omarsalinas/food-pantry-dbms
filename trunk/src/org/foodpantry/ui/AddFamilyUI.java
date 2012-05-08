package org.foodpantry.ui;


import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import org.foodpantry.db.DBConnection;

import java.sql.Connection;

public class AddFamilyUI {
	
	static JTextField lastNameTF;
	static JTextField primaryFirstNameTF;
	static JTextField noAdultsTF;
	static JTextField noChildrenTF;
	static JTextField houseNumberTF;
	static JTextField streetTF;
	static JTextField cityTF;
	static JTextField stateTF;
	static JTextField zipTF;
	
	static Connection conn;
	static java.sql.Date todaysDate = getTodaysDate(5, 4, 2012);
	static JFrame frame;

	
	//TODO get connection
	/**AddFamilyUI(){
		//this.conn = connection;
		DBConnection dbConn	= new DBConnection();
		conn = dbConn.getDBConnection();
		set date
	}*/
	
	//Needs to be changed to retrieve the correct date
	public static java.sql.Date getTodaysDate(int month, int date, int year){
		java.sql.Date fulldate = new java.sql.Date(0);
		fulldate.setDate(date);
		fulldate.setMonth(month - 1);
		fulldate.setYear(year - 1900);
		return fulldate;
	}
	
	public static void addComponentsToPane(Container pane) {
		
		DBConnection dbConn	= new DBConnection();
		conn = dbConn.getDBConnection();
		
		//set parent pane to springlayout
		SpringLayout layout = new SpringLayout();
		pane.setLayout(layout);
		
		// Add pane for directions
		JPanel status = AddFamilyUI.createStatusPane();
		pane.add(status);
			
		// Add pane for entering family data
        JPanel familyPanel = AddFamilyUI.createFamilyPanel();
        pane.add(familyPanel);		
			
        // Add pane for save and cancel buttons
		JPanel saveOrCancel = AddFamilyUI.createSaveOrCancel();
		pane.add(saveOrCancel);
        
		// Standard padding for the elements
		int padding = 5;
		// constraints on familyPanel
		layout.putConstraint(SpringLayout.WEST, familyPanel,
							padding,
							SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, familyPanel,
				 			50,
				 			SpringLayout.NORTH, pane);
		// constraints on the main contentpane
		layout.putConstraint(SpringLayout.EAST, pane, 
							 padding, 
							 SpringLayout.EAST, familyPanel);
		layout.putConstraint(SpringLayout.SOUTH, pane,
							 padding, 
							 SpringLayout.SOUTH, saveOrCancel);
		// constraints on saveOrCancel panel
		layout.putConstraint(SpringLayout.EAST, saveOrCancel, 
				 padding, 
				 SpringLayout.EAST, familyPanel);
		layout.putConstraint(SpringLayout.NORTH, saveOrCancel,
				 padding, 
				 SpringLayout.SOUTH, familyPanel);	
	}
	
	/**
	 * Create a status pane that displays the following
	 * 	-Welcome message for user
	 * 	-Connection status to server
	 *  -Administration button
	 */
	private static JPanel createStatusPane() {
		JPanel statusPane = new JPanel();
		JLabel text = new JLabel("Add the new family information here:");

		statusPane.add(text);
	
		return statusPane; 
	}
	
	private static JPanel createSaveOrCancel(){
		JPanel saveOrCancel = new JPanel();
		final JButton btnSave = new JButton("Save");
		saveOrCancel.add(btnSave);
		
        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            		
            		System.out.println("lastname::" + lastNameTF.getText());
            		//Make sure there are no blank fields
            		if (lastNameTF.getText().equals("")||primaryFirstNameTF.getText().equals("")
            				||noChildrenTF.getText().equals("")||noAdultsTF.getText().equals("")
            				|| streetTF.getText().equals("")||cityTF.getText().equals("")|| stateTF.getText().equals("")
            				||houseNumberTF.getText().equals("")||zipTF.getText().equals(""))
            			
            		{JOptionPane.showMessageDialog(btnSave,
        				    "There are blank fields on the form. Please try again.",
        				    "Blank Fields",
        				    JOptionPane.ERROR_MESSAGE);}
            		else{
            		
            			//Make sure address is in elkridge before trying to insert anything
            		if (cityTF.getText().equals("Elkridge") && (stateTF.getText().equals("MD") || 
            				stateTF.getText().equals("Maryland"))&& zipTF.getText().equals("21076"))
            		{
        			try {
        				PreparedStatement insertStatement = null;
        				String insertSQL = "INSERT INTO Family (Last_Name, Primary_Name, No_Children, No_Adults, Creation_Time) VALUES (?, ?, ?, ?, ?)";
        				insertStatement = conn.prepareStatement(insertSQL);  //having trouble with date
        				insertStatement.setString(1, lastNameTF.getText());
        				insertStatement.setString(2, primaryFirstNameTF.getText());
        				insertStatement.setInt(3, Integer.parseInt(noChildrenTF.getText()));
        				insertStatement.setInt(4, Integer.parseInt(noAdultsTF.getText()));
        				insertStatement.setDate(5, todaysDate);
        				insertStatement.executeUpdate();
        			}catch(NumberFormatException nFE)
        			{
        				//ensure no of children is numeric
        				JOptionPane.showMessageDialog(btnSave,
            				    "Number of Children and Number of Adults must be numeric.",
            				    "Input Error",
            				    JOptionPane.ERROR_MESSAGE);
        			}
        			catch (SQLException e1) {
        				
        				e1.printStackTrace();
        			}
        			
        			int familyNumber = 0;
        			try {
        				String querySQL = "SELECT LAST_INSERT_ID()";
        				PreparedStatement selectStatement = (PreparedStatement) conn.prepareStatement(querySQL);
        				ResultSet rs = selectStatement.executeQuery();
        				
        				if(rs.next()){
        					System.out.println("family number::" + rs.getInt(1));
        					familyNumber = rs.getInt(1);
        				}
        				else
        					System.out.println("ERROR: Last ID not found.");  
        			} catch (SQLException e2) {
        				e2.printStackTrace();
        			}
        			try {
        				PreparedStatement insertStatement = null;
        				String insertSQL = "INSERT INTO Family_Address (Family_Number, House_Number, Street, City, State, Zip, Creation_Time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        				insertStatement = conn.prepareStatement(insertSQL);  //having trouble with date
        				insertStatement.setInt(1, familyNumber);
        				insertStatement.setInt(2, Integer.parseInt(houseNumberTF.getText()));
        				insertStatement.setString(3, streetTF.getText());
        				insertStatement.setString(4, cityTF.getText());
        				insertStatement.setString(5, stateTF.getText());
        				insertStatement.setString(6, zipTF.getText());
        				insertStatement.setDate(7, todaysDate);
        				insertStatement.executeUpdate();
        				System.out.println("saved");
        			}

        			//ensure house number is numeric
        			catch(NumberFormatException nFE)
        			{
        				JOptionPane.showMessageDialog(btnSave,
            				    "House Number must be numeric.",
            				    "Input Error",
            				    JOptionPane.ERROR_MESSAGE);
        			} 
        			catch (SQLException e1) {
        				e1.printStackTrace();
        			}        			
            }
            		else
            		{System.out.println(cityTF.getText());
            		System.out.println(stateTF.getText());
            		System.out.println(zipTF.getText());
            			
            		// ensure city = Elkridge, state = MD, zip = 21076
            			JOptionPane.showMessageDialog(btnSave,
            				    "Only families from Elkridge, MD, 21076 are allowed.",
            				    "Address Error",
            				    JOptionPane.ERROR_MESSAGE);
            		}			
            }
            }});
		
		JButton btnCancel = new JButton("Cancel");
		saveOrCancel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	frame.dispose();
            }
		});
		return saveOrCancel;
	}
	
	private static JPanel createFamilyPanel(){
		JPanel familyPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
       
        cs.fill = GridBagConstraints.HORIZONTAL;
        
        //Last Name
        JLabel lastName;
        
        lastName = new JLabel("Last Name: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        familyPanel.add(lastName, cs);

        lastNameTF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 1;
        familyPanel.add(lastNameTF, cs);

        //Primary First Name
        JLabel primaryFirstName;
        
        primaryFirstName = new JLabel("Primary First Name: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        familyPanel.add(primaryFirstName, cs);

        primaryFirstNameTF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 1;
        familyPanel.add(primaryFirstNameTF, cs);
        familyPanel.setBorder(new LineBorder(Color.GRAY));

        //Number of Adults in the family
        JLabel noAdults;
        
    	noAdults = new JLabel("Number of Adults: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        familyPanel.add(noAdults, cs);

        noAdultsTF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 1;
        familyPanel.add(noAdultsTF, cs);
        familyPanel.setBorder(new LineBorder(Color.GRAY));
        
        //Number of Children in the family
        JLabel noChildren;
        
    	noChildren = new JLabel("Number of Children: ");
        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        familyPanel.add(noChildren, cs);

        noChildrenTF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 3;
        cs.gridwidth = 1;
        familyPanel.add(noChildrenTF, cs);
        familyPanel.setBorder(new LineBorder(Color.GRAY));
        
        //House Number
        JLabel houseNumber;
        
    	houseNumber = new JLabel("House Number: ");
        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 1;
        familyPanel.add(houseNumber, cs);

        houseNumberTF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 4;
        cs.gridwidth = 1;
        familyPanel.add(houseNumberTF, cs);
        familyPanel.setBorder(new LineBorder(Color.GRAY));
        
        //Street
        JLabel street;
       
    	street = new JLabel("Street: ");
        cs.gridx = 0;
        cs.gridy = 5;
        cs.gridwidth = 1;
        familyPanel.add(street, cs);

        streetTF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 5;
        cs.gridwidth = 1;
        familyPanel.add(streetTF, cs);
        familyPanel.setBorder(new LineBorder(Color.GRAY));
        
        //City
        JLabel city;
        
    	city = new JLabel("City: ");
        cs.gridx = 0;
        cs.gridy = 6;
        cs.gridwidth = 1;
        familyPanel.add(city, cs);

        cityTF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 6;
        cs.gridwidth = 1;
        familyPanel.add(cityTF, cs);
        familyPanel.setBorder(new LineBorder(Color.GRAY));
        
        
        //State
        JLabel state;
        
    	state = new JLabel("State: ");
        cs.gridx = 0;
        cs.gridy = 7;
        cs.gridwidth = 1;
        familyPanel.add(state, cs);

        stateTF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 7;
        cs.gridwidth = 1;
        familyPanel.add(stateTF, cs);
        familyPanel.setBorder(new LineBorder(Color.GRAY));
        
        //Zip
        JLabel zip;
        
    	zip = new JLabel("Zip: ");
        cs.gridx = 0;
        cs.gridy = 8;
        cs.gridwidth = 1;
        familyPanel.add(zip, cs);

        zipTF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 8;
        cs.gridwidth = 1;
        familyPanel.add(zipTF, cs);
        familyPanel.setBorder(new LineBorder(Color.GRAY));

		return familyPanel;
  	}

	/**
	 * Create and show the main GUI.  Should be implemented
	 * in a way to provide thread safety.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		frame = new JFrame("Add A New Family");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addComponentsToPane(frame.getContentPane());
		
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	//temp main, to be removed once integrated with the rest of the program
	public static void main(String[] args) {
		// Invoke the thread with 
		// Create and show the main GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AddFamilyUI.createAndShowGUI();
			}
		});
	}
	
}
