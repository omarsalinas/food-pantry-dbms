import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * The main GUI.
 */
public class mainUI {
	
	private static JTable table;
	
	/**
	 * Add all of the components to the pane to create the main GUI.
	 * @param pane
	 */
	public static void addComponentsToPane(Container pane) {
		// Create the model for the table
		familyTableModel model = new familyTableModel();
		// Initialize the table and add the model to it
		table = new JTable(model);
		// Create a scrollpane and add the table to it
		JScrollPane scrollPane = new JScrollPane(table);
		
		// Add the scrollpane to the pane
		pane.add(scrollPane);
	}
	
	/**
	 * Create and show the main GUI.  Should be implemented
	 * in a way to provide thread safety.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("GridBagLayoutDemo");
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
				mainUI.createAndShowGUI();
			}
			
		});
	}
}

