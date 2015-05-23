package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

class LeafImage extends JLabel {
	private static final long serialVersionUID = -7639003929835462965L;

	public LeafImage( BufferedImage img) {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setIcon( new ButtonIcon( img));
		setPreferredSize( new Dimension( img.getWidth(), img.getHeight()));
		setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
	}
}
