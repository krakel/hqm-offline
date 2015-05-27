package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JLabel;

public class LeafIcon extends JLabel {
	private static final long serialVersionUID = -6010940963328765201L;

	public LeafIcon( Image back) {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		Dimension size = new Dimension( AEntity.ZOOM * back.getWidth( null), AEntity.ZOOM * back.getHeight( null));
		setPreferredSize( size);
		setMinimumSize( size.getSize());
		setMaximumSize( size.getSize());
	}

	public LeafIcon( StackIcon icon) {
		this( icon.getBack());
		setIcon( icon);
	}
}
