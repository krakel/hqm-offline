package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.utils.ResourceManager;

public class LeafIcon extends JLabel {
	private static final long serialVersionUID = -6010940963328765201L;

	public LeafIcon( AStack stk) {
		this( countOf( stk), ResourceManager.getImageUI( "hqm.icon.back"), imageOf( stk));
	}

	public LeafIcon( BufferedImage back, BufferedImage img) {
		this( null, back, img);
	}

	public LeafIcon( String text, BufferedImage back, BufferedImage img) {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setIcon( new StackIcon( text, back, img));
		Dimension size = new Dimension( AEntity.ZOOM * back.getWidth(), AEntity.ZOOM * back.getHeight());
		setPreferredSize( size);
		setMaximumSize( size.getSize());
	}

	private static String countOf( AStack stk) {
		return stk != null ? stk.getCount() : null;
	}

	private static BufferedImage imageOf( AStack stk) {
		return stk != null ? stk.getImage() : null;
	}
}
