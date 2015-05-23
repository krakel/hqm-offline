package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import de.doerl.hqm.utils.ResourceManager;

class LeafButton extends JLabel {
	private static final long serialVersionUID = -1227707973285499866L;

	public LeafButton( String text) {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		BufferedImage img = ResourceManager.getImageUI( "hqm.button");
		setIcon( new ButtonIcon( text, img));
		Dimension size = new Dimension( AEntity.ZOOM * img.getWidth(), AEntity.ZOOM * img.getHeight());
		setMinimumSize( size.getSize());
		setPreferredSize( size);
	}
}
