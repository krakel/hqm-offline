package de.doerl.hqm.ui.treetable;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.utils.Utils;

class SelectHandler implements MouseListener {
	private static final Logger LOGGER = Logger.getLogger( SelectHandler.class.getName());
	private EditController mCtrl;

	public SelectHandler( EditController ctrl) {
		mCtrl = ctrl;
	}

	private void activate( Object src, MouseEvent evt) {
		try {
			TreeTable table = (TreeTable) src;
			int row = table.rowAtPoint( evt.getPoint());
			if (row >= 0) {
				TreeTableModel model = table.getModel();
				ATreeTableRow node = model.getRow( row);
				ABase base = node.getElementObject();
				mCtrl.setActive( base);
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void mouseClicked( MouseEvent evt) {
		if (evt.isConsumed()) {
			return;
		}
		Object src = evt.getSource();
		if (src == null) {
			Utils.log( LOGGER, Level.WARNING, "Missing.source");
		}
		else if (evt.getClickCount() == 2) {
//			activate( src, evt);
		}
	}

	@Override
	public void mouseEntered( MouseEvent evt) {
	}

	@Override
	public void mouseExited( MouseEvent evt) {
	}

	@Override
	public void mousePressed( MouseEvent evt) {
	}

	@Override
	public void mouseReleased( MouseEvent evt) {
		if (evt.isConsumed()) {
			return;
		}
		Object src = evt.getSource();
		if (src == null) {
			Utils.log( LOGGER, Level.WARNING, "Missing.source");
		}
		else {
			activate( src, evt);
		}
	}
}
