package de.doerl.hqm.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

class LeafTextBox extends JTextArea {
	private static final long serialVersionUID = 535359109100667359L;
	private static final Border BORDER = BorderFactory.createBevelBorder( BevelBorder.RAISED);
	private ClickHandler mHandler = new ClickHandler();
	private Border mOldBorder;

	public LeafTextBox() {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setFont( AEntity.FONT_NORMAL);
		setLineWrap( true);
		setWrapStyleWord( true);
		setEditable( false);
		addMouseListener( mHandler);
		addMouseListener( new MouseAdapter() {
			@Override
			public void mouseEntered( MouseEvent e) {
				onEntered();
			}

			@Override
			public void mouseExited( MouseEvent e) {
				onExited();
			}
		});
	}

	public void addClickListener( IClickListener l) {
		mHandler.addClickListener( l);
	}

	private void onEntered() {
		mOldBorder = getBorder();
		setBorder( BORDER);
	}

	private void onExited() {
		setBorder( mOldBorder);
		mOldBorder = null;
	}

	public void removeClickListener( IClickListener l) {
		mHandler.removeClickListener( l);
	}
}
