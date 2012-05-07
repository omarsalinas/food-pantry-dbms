package org.foodpantry.ui;

import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

public class SelectFamilyForWaitlistUI extends JFrame {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -1534413015745702353L;
	
	/**
	 * Title of JFrame
	 */
	private String title = "Add a family to the waitlist.";

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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout layout = new SpringLayout();
		pane.setLayout(layout);
		
		FamilyTableModel model = new FamilyTableModel();
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(
				scrollPane.getBorder(), "Families",
				TitledBorder.LEFT, TitledBorder.TOP));
		
		pane.add(scrollPane);
		
		JButton addFamily = new JButton("Add New Family");
		pane.add(addFamily);
		JButton editFamily = new JButton("Edit Family");
		pane.add(editFamily);
		
		int padding = 5;
		// Scrollpane constraints
		layout.putConstraint(SpringLayout.WEST, scrollPane,
				 padding,
				 SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, scrollPane,
				 padding,
				 SpringLayout.NORTH, pane);
		layout.putConstraint(SpringLayout.EAST, pane, 
				 padding, 
				 SpringLayout.EAST, editFamily);
		// Edit family constraints
		layout.putConstraint(SpringLayout.WEST, editFamily,
				 padding,
				 SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.SOUTH, pane,
				 padding, 
				 SpringLayout.SOUTH, editFamily);
		// Add family constraints
		layout.putConstraint(SpringLayout.WEST, addFamily,
				 padding,
				 SpringLayout.WEST, editFamily);
		layout.putConstraint(SpringLayout.SOUTH, pane,
				 padding, 
				 SpringLayout.SOUTH, addFamily);
		
		this.pack();
	}

}
