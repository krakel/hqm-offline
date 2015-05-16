package de.doerl.hqm.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.utils.Utils;

public class SelectHandler implements MouseListener {
	private static final Logger LOGGER = Logger.getLogger( SelectHandler.class.getName());
	private EditController mCtrl;

	public SelectHandler( EditController ctrl) {
		mCtrl = ctrl;
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
			mCtrl.activate( src, ev);
		}
	}
}
