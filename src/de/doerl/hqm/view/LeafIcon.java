package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JLabel;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.utils.ResourceManager;

public class LeafIcon extends JLabel {
	private static final long serialVersionUID = -6010940963328765201L;
	private Image mBack;

	public LeafIcon( AStack stk) {
		this( ResourceManager.getImageUI( "hqm.icon.back"));
		setIcon( stk);
	}

	private LeafIcon( Image back) {
		mBack = back;
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		Dimension size = new Dimension( AEntity.ZOOM * back.getWidth( null), AEntity.ZOOM * back.getHeight( null));
		setPreferredSize( size);
		setMinimumSize( size.getSize());
		setMaximumSize( size.getSize());
	}

	public LeafIcon( String text, Image back, Image img) {
		this( back);
		setIcon( text, img);
	}

	private static String countOf( AStack stk) {
		if (stk != null) {
			int count = stk.getCount();
			return count > 1 ? Integer.toString( count) : null;
		}
		else {
			return null;
		}
	}

	public void setIcon( AStack stk) {
		super.setIcon( new StackIcon( countOf( stk), mBack, null));
	}

	public void setIcon( String text, Image img) {
		super.setIcon( new StackIcon( text, mBack, img));
	}
}
