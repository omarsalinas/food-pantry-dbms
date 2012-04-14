import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * 
 * The main purpose os this class is to handle the drag and drop functionality
 * within the waitlist.  This should allow the selected row to moved to a new
 * position within the <code>JTable</code>.
 *
 */
public class WaitListTransferHandler extends TransferHandler {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 5632344621735413292L;

	private JTable table = null;
	
	public WaitListTransferHandler(JTable table) {
		this.table = table;
	}
	
}
