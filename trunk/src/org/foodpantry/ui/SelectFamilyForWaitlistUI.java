package org.foodpantry.ui;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

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
//		try {
////			model = new FamilyTableModel();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		JTable table = new JTable(model);
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

		JButton addNewFamilyBtn = new JButton("Add New Family");
		addNewFamilyBtn.addActionListener(this); 
		addNewFamilyBtn.setActionCommand("Add New Family");
		addNewFamilyBtn.setMnemonic(KeyEvent.VK_A);
		pane.add(addNewFamilyBtn);
		
		JButton editFamilyBtn = new JButton("Edit Family Information");
		editFamilyBtn.addActionListener(this); 
		editFamilyBtn.setActionCommand("Edit Family");
		editFamilyBtn.setMnemonic(KeyEvent.VK_E);
		pane.add(editFamilyBtn);
	
		return pane; 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add New Family")) {
			AddNewFamilyUI window = new AddNewFamilyUI();
		} else if (e.getActionCommand().equals("Edit Family")) {
			//TODO have something for editing family information
		}
	}

}
