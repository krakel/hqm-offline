package de.doerl.hqm.view;

import java.awt.Dimension;

import javax.swing.JLabel;

public class LeafIcon extends JLabel {
	private static final long serialVersionUID = -6010940963328765201L;

	public LeafIcon( Dimension size) {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setPreferredSize( size.getSize());
		setMinimumSize( size.getSize());
		setMaximumSize( size.getSize());
	}

	public LeafIcon( StackIcon icon) {
		this( AEntity.sizeOf( icon.getBack()));
		setIcon( icon);
	}
}
