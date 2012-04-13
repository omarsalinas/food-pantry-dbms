import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

/**
 * The main GUI.
 */
public class MainUI {
	
	/**
	 * Table to display the waitlist
	 */
	private static JTable waitTable;
	
	/**
	 * Value to show if user is logged in.
	 * Initialized to false, for security.
	 */
	private boolean isLoggedIn = false;
	
	/**
	 * Value to show if user is an administrator
	 * Initialized to false, for security.
	 */
	private boolean isAdministrator = false;
	
	/**
	 * Add all of the components to the pane to create the main GUI.
	 * @param pane
	 */
	public static void addComponentsToPane(Container pane) {
		// Create the model for the table
		WaitTableModel model = new WaitTableModel();
		// Initialize the family table and add the model to it
		waitTable = new JTable(model);
		// Allow the table to support drag and drop
		// TODO setup the rest of the drag and drop functionality
		waitTable.setDragEnabled(true);
		// Create a scrollpane and add the table to it
		JScrollPane scrollPane = new JScrollPane(waitTable);
		scrollPane.setBorder(BorderFactory.createTitledBorder(
				waitTable.getBorder(), "Waitlist",
				TitledBorder.LEFT, TitledBorder.TOP));
		
		// Add the scrollpane to the pane
		pane.add(scrollPane);
	}
	
	/**
	 * Create and show the main GUI.  Should be implemented
	 * in a way to provide thread safety.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Food Pantry");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Set up the content pane.
		addComponentsToPane(frame.getContentPane());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		// Invoke the thread with 
		// Create and show the main GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainUI.createAndShowGUI();
			}
			
		});
	}
}