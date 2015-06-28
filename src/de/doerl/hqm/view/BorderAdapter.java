package de.doerl.hqm.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

class BorderAdapter extends MouseAdapter {
	private static final Border BORDER = BorderFactory.createBevelBorder( BevelBorder.RAISED);
	private Border mOldBorder;
	private JComponent mComp;

	public BorderAdapter( JComponent comp) {
		mComp = comp;
	}

	@Override
	public void mouseEntered( MouseEvent e) {
		if (mComp.isEnabled()) {
			mOldBorder = mComp.getBorder();
			mComp.setBorder( BORDER);
		}
	}

	@Override
	public void mouseExited( MouseEvent e) {
		if (mComp.isEnabled()) {
			mComp.setBorder( mOldBorder);
			mOldBorder = null;
		}
	}
}
