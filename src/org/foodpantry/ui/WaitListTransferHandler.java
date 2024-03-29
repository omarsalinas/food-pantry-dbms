package org.foodpantry.ui;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * 
 * The main purpose of this class is to handle the drag and drop functionality
 * within the waitlist. This should allow the selected row to moved to a new
 * position within the <code>JTable</code>.
 * 
 * Code mostly borrowed from:
 * http://stackoverflow.com/questions/638807/how-do-i-
 * drag-and-drop-a-row-in-a-jtable
 * http://stackoverflow.com/questions/715332/jtable-drag-and-drop
 * 
 */
@SuppressWarnings("serial")
public class WaitListTransferHandler extends TransferHandler {
	/**
	 * Handles drag & drop row reordering
	 */
	private final DataFlavor localObjectFlavor = new ActivationDataFlavor(
			Integer.class, DataFlavor.javaJVMLocalObjectMimeType,
			"Integer Row Index");
	private JTable table = null;

	public WaitListTransferHandler(JTable table) {
		this.table = table;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		assert (c == table);
		return new DataHandler(new Integer(table.getSelectedRow()),
				localObjectFlavor.getMimeType());
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		boolean b = info.getComponent() == table && info.isDrop()
				&& info.isDataFlavorSupported(localObjectFlavor);
		table.setCursor(b ? DragSource.DefaultMoveDrop
				: DragSource.DefaultMoveNoDrop);
		return b;
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		JTable target = (JTable) info.getComponent();
		JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
		int index = dl.getRow();
		int max = table.getModel().getRowCount();
		if (index < 0 || index > max)
			index = max;
		target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		try {
			Integer rowFrom = (Integer) info.getTransferable().getTransferData(
					localObjectFlavor);
			if (rowFrom != -1 && rowFrom != index) {
				((Reorderable) table.getModel()).reorder(rowFrom, index);
				if (index > rowFrom)
					index--;
				target.getSelectionModel().addSelectionInterval(index, index);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void exportDone(JComponent c, Transferable t, int act) {
		if (act == TransferHandler.MOVE) {
			table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

}