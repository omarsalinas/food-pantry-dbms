package org.foodpantry.ui;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class PopUpErrorWindow extends JFrame implements ActionListener{

		/**
		 * Generated Serial Version UID
		 */
		private static final long serialVersionUID = -1534413015745702353L;
		
		/**
		 * Title of JFrame
		 */
		private String title = "Error!";

		/**
		 * Constructs and adds all needed elements.  Also, packs the frame.
		 * Does not, however, set the frame to visible.
		 * @throws HeadlessException
		 */
		public PopUpErrorWindow(String error) throws HeadlessException {
			// Get the container to make following code a little easier to read
			Container pane = this.getContentPane();
			
			// Set JFrame Configuration
			this.setTitle(title);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			SpringLayout layout = new SpringLayout();
			pane.setLayout(layout);
			
			JPanel errorPane = new JPanel();
			JLabel text = new JLabel(error);
			errorPane.add(text);
			pane.add(errorPane);
			
			
			int padding = 5;
			// Add family constraints
			layout.putConstraint(SpringLayout.WEST, errorPane,
					 padding,
					 SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.NORTH, errorPane,
					 padding, 
					 SpringLayout.NORTH, pane);
			// pane constraints
			layout.putConstraint(SpringLayout.SOUTH, pane,
					 padding, 
					 SpringLayout.SOUTH, errorPane);
			layout.putConstraint(SpringLayout.EAST, pane, 
					 padding, 
					 SpringLayout.EAST, errorPane);
			
			this.pack();
		}

		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
		}

	
}
