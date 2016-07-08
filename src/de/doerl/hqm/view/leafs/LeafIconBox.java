package de.doerl.hqm.view.leafs;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import de.doerl.hqm.view.BorderAdapter;
import de.doerl.hqm.view.ClickHandler;
import de.doerl.hqm.view.StackIcon;

public class LeafIconBox extends JLabel {
	private static final long serialVersionUID = 3718314366309841477L;
	private ClickHandler mHandler = new ClickHandler();

	public LeafIconBox() {
		this( StackIcon.ICON_SIZE);
	}

	public LeafIconBox( Dimension size) {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setPreferredSize( size.getSize());
		setMinimumSize( size.getSize());
		setMaximumSize( size.getSize());
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
