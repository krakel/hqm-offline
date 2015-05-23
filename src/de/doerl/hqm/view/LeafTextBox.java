package de.doerl.hqm.view;

import java.awt.event.ActionListener;
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
				mOldBorder = getBorder();
				setBorder( BORDER);
			}

			@Override
			public void mouseExited( MouseEvent e) {
				setBorder( mOldBorder);
				mOldBorder = null;
			}
		});
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}
}
