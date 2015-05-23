package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

class LeafTextField extends JLabel {
	private static final long serialVersionUID = 5262142469277711143L;
	private ClickHandler mHandler = new ClickHandler();

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
		setPreferredSize( new Dimension( 200, 2 * getFont().getSize()));
		setMaximumSize( new Dimension( Short.MAX_VALUE, getFont().getSize()));
		addMouseListener( mHandler);
		addMouseListener( new BorderAdapter( this));
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}
}
