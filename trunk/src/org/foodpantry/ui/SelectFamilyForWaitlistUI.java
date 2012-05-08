package org.foodpantry.ui;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

/**
 * Window that lets the user choose someone to add to the waitlist.
 * TODO needs to enforce that family credentials are good before adding
 */
public class SelectFamilyForWaitlistUI extends JFrame implements ActionListener{

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
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout layout = new SpringLayout();
		pane.setLayout(layout);
		
		FamilyTableModel model = null;
		try {
			model = new FamilyTableModel();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				 SpringLayout.SOUTH, editFamily);
		// Edit family constraints
		layout.putConstraint(SpringLayout.WEST, editFamily,
				 padding,
				 SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, editFamily,
				 padding, 
				 SpringLayout.NORTH, pane);
		// Add family constraints
		layout.putConstraint(SpringLayout.WEST, addFamily,
				 padding,
				 SpringLayout.EAST, editFamily);
		layout.putConstraint(SpringLayout.NORTH, addFamily,
				 padding, 
				 SpringLayout.NORTH, pane);
		// pane constraints
		layout.putConstraint(SpringLayout.SOUTH, pane,
				 padding, 
				 SpringLayout.SOUTH, scrollPane);
		layout.putConstraint(SpringLayout.EAST, pane, 
				 padding, 
				 SpringLayout.EAST, scrollPane);
		
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
