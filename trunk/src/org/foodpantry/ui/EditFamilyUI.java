package org.foodpantry.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import org.foodpantry.db.DBConnection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditFamilyUI extends JFrame implements ActionListener{
		
		private static final long serialVersionUID = 1L;
		static JTextField lastNameTF = new JTextField(20);;
		static JTextField primaryFirstNameTF = new JTextField(20);
		static JTextField noAdultsTF = new JTextField(20);
		static JTextField noChildrenTF = new JTextField(20);
		static JTextField houseNumberTF = new JTextField(20);
		static JTextField streetTF = new JTextField(20);
		static JTextField cityTF = new JTextField(20);
		static JTextField stateTF = new JTextField(20);
		static JTextField zipTF = new JTextField(20);
		
		static List<String []> addressList = new ArrayList<String []>();	
		static List<String> dropdownAddrList = new ArrayList<String>();
		static Connection conn;
		static java.sql.Date todaysDate = new java.sql.Date(System.currentTimeMillis());
		static JFrame frame;

		
		//TODO get connection
		//public AddNewFamilyUI(){
			//this.conn = connection;
			//DBConnection dbConn	= new DBConnection();
			//conn = dbConn.getDBConnection();
			//set date
		//}
		
		public EditFamilyUI() {
			
			Container pane = this.getContentPane();
			
			// Set JFrame Configuration
			this.setTitle("Edit Family");
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			DBConnection dbConn	= new DBConnection();
			conn = dbConn.getDBConnection();
			
			refreshFamilyInfo();

			//set parent pane to springlayout
			SpringLayout layout = new SpringLayout();
			pane.setLayout(layout);
			
			// Add pane for directions
			JPanel status = EditFamilyUI.createStatusPane();
			pane.add(status);
				
			// Add pane for entering family data
	        JPanel familyPanel = EditFamilyUI.createFamilyPanel();
	        pane.add(familyPanel);		
				
	        // Add pane for save and cancel buttons
			JPanel saveOrCancel = EditFamilyUI.createSaveOrCancel();
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
			
			this.pack();
		}
		
		/**
		 * Create a status pane that displays the following
		 * 	-Welcome message for user
		 * 	-Connection status to server
		 *  -Administration button
		 */
		private static JPanel createStatusPane() {
			JPanel statusPane = new JPanel();
			JLabel text = new JLabel("Edit the family information here:");

			statusPane.add(text);
		
			return statusPane; 
		}
		
		private static void refreshFamilyInfo(){
			
			int familyNumber = 1;
			// Iterate over the list of families
			for (int i=0 ; i<SelectFamilyForWaitlistUI.model.getRowCount(); i++) {
				// if a row was selected, append to the sql statement
				if ((Boolean)SelectFamilyForWaitlistUI.model.getValueAt(i, 0) == true ) {
					// once a selection is found, all following will need a comma
					// prepended
					familyNumber = (Integer) SelectFamilyForWaitlistUI.model.getValueAt(i,1);
					break;
				}
			}
			
			PreparedStatement selectStatement;
			ResultSet resultSet;
			
			String querySQL = "SELECT Last_Name, Primary_Name, No_Children, No_Adults FROM Family WHERE Family_Number = ?";
			try {
				selectStatement = conn.prepareStatement(querySQL);
				selectStatement.setInt(1, familyNumber);
				resultSet = selectStatement.executeQuery();
				if(resultSet.next()){
					lastNameTF.setText(resultSet.getString("Last_Name"));
					primaryFirstNameTF.setText(resultSet.getString("Primary_Name"));
					noAdultsTF.setText(resultSet.getString("No_Adults"));
					noChildrenTF.setText(resultSet.getString("No_Children"));
				}
				
				querySQL = "SELECT House_Number, Street, City, State, Zip, Current FROM Family_Address WHERE Family_Number = ?";
				selectStatement = conn.prepareStatement(querySQL);
				selectStatement.setInt(1, familyNumber);
				resultSet = selectStatement.executeQuery();
				Boolean first = true;
				while(resultSet.next()){
					String houseNum = resultSet.getString("House_Number");
					String street = resultSet.getString("Street");
			        String[] addr = {
			        		houseNum,
			        		street, 
			        		resultSet.getString("City"),
			        		resultSet.getString("State"),
			        		resultSet.getString("Zip"),
			        		resultSet.getString("Current")
			       };
			        if(first == true){
			    		houseNumberTF.setText(addr[0]);
			    		streetTF.setText(addr[1]);
			    		cityTF.setText(addr[2]);
			    		stateTF.setText(addr[3]);
			    		zipTF.setText(addr[4]);
			    		first = false;
			        }
			        addressList.add(addr);
			        dropdownAddrList.add(houseNum + " " + street);
				}
				dropdownAddrList.add("Add New Address");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	            			|| streetTF.getText().equals("")||cityTF.getText().equals("")
	            			|| stateTF.getText().equals("")	||houseNumberTF.getText().equals("")
	            			||zipTF.getText().equals(""))
	            		{JOptionPane.showMessageDialog(btnSave, "There are blank fields on the form. Please try again.", "Blank Fields",
	            			JOptionPane.ERROR_MESSAGE);
	            		} 
	            		else if(cityTF.getText().equals("Elkridge") && (stateTF.getText().equals("MD") ||
	            			stateTF.getText().equals("Maryland"))&& zipTF.getText().equals("21076")){
	        	
	        
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
	        			} 
	        			catch(NumberFormatException nFE){
	        				
	        				//ensure no of children  & adults is numeric
	        				JOptionPane.showMessageDialog(btnSave, "Number of Children and Number" +
	        						" of Adults must be numeric.", "Input Error",
	        						JOptionPane.ERROR_MESSAGE);
	        			}
	        			catch (SQLException e1) {
	        				// TODO Auto-generated catch block
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
	    			
	        			// TODO
	        			// ensure city = Elkridge, state = MD, zip = 21076
	        			/*try {
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
	        			} 
	        			//ensure house number is numeric
	        			catch(NumberFormatException nFE)
	        			{
	        				JOptionPane.showMessageDialog(btnSave, "House Number must be numeric.",
	        						"Input Error", JOptionPane.ERROR_MESSAGE);
	        			}
	        			catch (SQLException e1) {
	        				// TODO Auto-generated catch block
	        				e1.printStackTrace();
	        			}*/     
	        }
	        else{
	        	
	        	// ensure city = Elkridge, state = MD, zip = 21076 
	        	JOptionPane.showMessageDialog(btnSave,
	        	"Only families from Elkridge, MD, 21076 are allowed.", 
	        	"Address Error",  JOptionPane.ERROR_MESSAGE); 
	        }
	            }
	        });
			
	        JButton saveAddressBtn = new JButton("Save Address");
			saveOrCancel.add(saveAddressBtn);
			saveAddressBtn.addActionListener(new ActionListener() {
				String houseNum = houseNumberTF.getText();
				String street = streetTF.getText();
	            public void actionPerformed(ActionEvent e) {

	            /*try{
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
    			} 
    			//ensure house number is numeric
    			catch(NumberFormatException nFE)
    			{
    				JOptionPane.showMessageDialog(btnSave, "House Number must be numeric.",
    						"Input Error", JOptionPane.ERROR_MESSAGE);
    			}
    			catch (SQLException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}*/
	            	
	            	System.out.println("here");
		    		String [] addrArray ={
	            	houseNum,
	            	street,
		    		cityTF.getText(),
		    		stateTF.getText(),
		    		zipTF.getText()};
		        addressList.add(addrArray);
		        dropdownAddrList.add(houseNum + " " + street);
	            } 
			});
	        
			/*JButton btnCancel = new JButton("Cancel");
			saveOrCancel.add(btnCancel);
			btnCancel.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	//dispose();
	            }
			});*/
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

	        // TODO lastNameTF.setText("test");
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

	        cs.gridx = 1;
	        cs.gridy = 3;
	        cs.gridwidth = 1;
	        familyPanel.add(noChildrenTF, cs);
	        familyPanel.setBorder(new LineBorder(Color.GRAY));
	        
	        //Address drop down menu
	        JLabel addresses;
	        
	        addresses = new JLabel("Addesses: ");
	        cs.gridx = 0;
	        cs.gridy = 4;
	        cs.gridwidth = 1;
	        familyPanel.add(addresses, cs);
	        
	       // dropdownAddrList

		    JComboBox addressDropDown = new JComboBox(dropdownAddrList.toArray());
		    addressDropDown.setEditable(true);
		    //addressDropDown.addActionListener(new AddressComboBoxListener());
		    addressDropDown.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	String selectItem =((JComboBox) e.getSource()).getSelectedItem().toString();
	            	System.out.println("Selected::" + selectItem);
	    			if(addressList != null){
	    				Iterator<String[]> addrItr = addressList.iterator();
	    				while(addrItr.hasNext()){
	    					String [] addr = addrItr.next();
	    					String houseNum = addr[0];
	    					String street = addr[1];
	    					String comp = new String(houseNum + " " + street);
	    					if(selectItem.equals(comp)){
	    						houseNumberTF.setText(addr[0]);
	    						streetTF.setText(addr[1]);
	    						cityTF.setText(addr[2]);
	    						stateTF.setText(addr[3]);
	    						zipTF.setText(addr[4]);
	    						return;
	    					}
	    					else if(selectItem.equals("Add New Address")){
	    						houseNumberTF.setText("");
	    						streetTF.setText("");
	    						cityTF.setText("");
	    						stateTF.setText("");
	    						zipTF.setText("");
	    						return;
	    					}
	    				}
	    			}
	            }
			});
	        cs.gridx = 1;
	        cs.gridy = 4;
	        cs.gridwidth = 1;
	        familyPanel.add(addressDropDown, cs);
	        familyPanel.setBorder(new LineBorder(Color.GRAY));
	        
	        //House Number
	        JLabel houseNumber;
	        
	    	houseNumber = new JLabel("House Number: ");
	        cs.gridx = 0;
	        cs.gridy = 5;
	        cs.gridwidth = 1;
	        familyPanel.add(houseNumber, cs);

	        cs.gridx = 1;
	        cs.gridy = 5;
	        cs.gridwidth = 1;
	        familyPanel.add(houseNumberTF, cs);
	        familyPanel.setBorder(new LineBorder(Color.GRAY));
	        
	        //Street
	        JLabel street;
	       
	    	street = new JLabel("Street: ");
	        cs.gridx = 0;
	        cs.gridy = 6;
	        cs.gridwidth = 1;
	        familyPanel.add(street, cs);

	        cs.gridx = 1;
	        cs.gridy = 6;
	        cs.gridwidth = 1;
	        familyPanel.add(streetTF, cs);
	        familyPanel.setBorder(new LineBorder(Color.GRAY));
	        
	        //City
	        JLabel city;
	        
	    	city = new JLabel("City: ");
	        cs.gridx = 0;
	        cs.gridy = 7;
	        cs.gridwidth = 1;
	        familyPanel.add(city, cs);

	        cs.gridx = 1;
	        cs.gridy = 7;
	        cs.gridwidth = 1;
	        familyPanel.add(cityTF, cs);
	        familyPanel.setBorder(new LineBorder(Color.GRAY));
	        
	        
	        //State
	        JLabel state;
	        
	    	state = new JLabel("State: ");
	        cs.gridx = 0;
	        cs.gridy = 8;
	        cs.gridwidth = 1;
	        familyPanel.add(state, cs);

	        cs.gridx = 1;
	        cs.gridy = 8;
	        cs.gridwidth = 1;
	        familyPanel.add(stateTF, cs);
	        familyPanel.setBorder(new LineBorder(Color.GRAY));
	        
	        //Zip
	        JLabel zip;
	        
	    	zip = new JLabel("Zip: ");
	        cs.gridx = 0;
	        cs.gridy = 9;
	        cs.gridwidth = 1;
	        familyPanel.add(zip, cs);

	        cs.gridx = 1;
	        cs.gridy = 9;
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
			frame = new JFrame("Edit Family");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			//addComponentsToPane(frame.getContentPane());
			
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
					EditFamilyUI.createAndShowGUI();
				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("here");
		}
}

class AddressComboBoxListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(((JComboBox) e.getSource()).getSelectedItem());
	}
	
}