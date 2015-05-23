package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.utils.ResourceManager;

public class LeafIcon extends JLabel {
	private static final long serialVersionUID = -6010940963328765201L;
	private BufferedImage mBack;

	public LeafIcon( AStack stk) {
		this( ResourceManager.getImageUI( "hqm.icon.back"));
		setIcon( stk);
	}

	private LeafIcon( BufferedImage back) {
		mBack = back;
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		Dimension size = new Dimension( AEntity.ZOOM * back.getWidth(), AEntity.ZOOM * back.getHeight());
		setPreferredSize( size);
		setMinimumSize( size.getSize());
		setMaximumSize( size.getSize());
	}

	public LeafIcon( String text, BufferedImage back, BufferedImage img) {
		this( back);
		setIcon( text, img);
	}

	private static String countOf( AStack stk) {
		return stk != null ? stk.getCount() : null;
	}

	private static BufferedImage imageOf( AStack stk) {
		return stk != null ? stk.getImage() : null;
	}

	public void setIcon( AStack stk) {
		super.setIcon( new StackIcon( countOf( stk), mBack, imageOf( stk)));
	}

	public void setIcon( String text, BufferedImage img) {
		super.setIcon( new StackIcon( text, mBack, img));
	}
}
