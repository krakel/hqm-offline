package de.doerl.hqm.ui.tree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

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
			ElementTree tree = (ElementTree) src;
			TreePath path = tree.getPathForLocation( evt.getX(), evt.getY());
			if (path != null) {
				tree.scrollPathToVisible( path);
				Object obj = path.getLastPathComponent();
				if (obj != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
					ANode user = (ANode) node.getUserObject();
					mCtrl.setActive( user.getBase());
				}
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
