package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

class LeafTextField extends JLabel {
	private static final long serialVersionUID = 5262142469277711143L;
	private static final Border BORDER = BorderFactory.createBevelBorder( BevelBorder.RAISED);
	private ClickHandler mHandler = new ClickHandler();
	private Border mOldBorder;

	public LeafTextField() {
		this( Color.BLACK, false);
	}

	public LeafTextField( boolean title) {
		this( Color.BLACK, title);
	}

	public LeafTextField( Color color) {
		this( color, false);
	}

	public LeafTextField( Color color, boolean title) {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setFont( title ? AEntity.FONT_TITLE : AEntity.FONT_NORMAL);
		setForeground( color);
		setPreferredSize( new Dimension( Short.MAX_VALUE, getFont().getSize()));
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
