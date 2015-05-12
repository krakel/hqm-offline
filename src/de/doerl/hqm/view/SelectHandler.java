package de.doerl.hqm.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.ui.treetable.ATreeTableRow;
import de.doerl.hqm.ui.treetable.TreeTable;
import de.doerl.hqm.ui.treetable.TreeTableModel;
import de.doerl.hqm.utils.Utils;

public class SelectHandler implements MouseListener {
	private static final Logger LOGGER = Logger.getLogger( SelectHandler.class.getName());
	private EditController mController;

	public SelectHandler( EditController ctrl) {
		mController = ctrl;
	}

	private void activate( Object src, MouseEvent ev) {
		try {
			TreeTable table = (TreeTable) src;
			int row = table.rowAtPoint( ev.getPoint());
			if (row >= 0) {
				TreeTableModel model = (TreeTableModel) table.getModel();
				ATreeTableRow node = model.getRow( row);
				ABase base = node.getElementObject();
				SwingUtilities.invokeLater( new BaseAction( base));
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void mouseClicked( MouseEvent ev) {
		Object src = ev.getSource();
		if (src == null) {
			Utils.log( LOGGER, Level.WARNING, "Missing.source");
		}
		else if (ev.getClickCount() == 2) {
//			activate( src, ev);
		}
	}

	@Override
	public void mouseEntered( MouseEvent ev) {
	}

	@Override
	public void mouseExited( MouseEvent ev) {
	}

	@Override
	public void mousePressed( MouseEvent ev) {
	}

	@Override
	public void mouseReleased( MouseEvent ev) {
		Object src = ev.getSource();
		if (src == null) {
			Utils.log( LOGGER, Level.WARNING, "Missing.source");
		}
		else {
			activate( src, ev);
		}
	}

	private class BaseAction implements Runnable {
		private ABase mBase;

		public BaseAction( ABase base) {
			mBase = base;
		}

		@Override
		public void run() {
			mController.setActive( mBase);
		}
	}
}
