package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.utils.ResourceManager;

class StackIcon implements Icon {
	public static final Image ICON_BACK = ResourceManager.getImageUI( "hqm.icon.back");
	private Image mBack;
	private Image mImage;
	private String mText;
	private double mZoom;

	public StackIcon( AStack stk) {
		this( StackIcon.ICON_BACK, null, 0.6);
		mText = stk != null ? stk.countOf() : null;
	}

	public StackIcon( Image back, Image img, double zoom) {
		mBack = back;
		mImage = img != null ? img : ResourceManager.getImageUI( "hqm.unknown");
		mZoom = zoom;
	}

	public StackIcon( String txt, Image img, double zoom) {
		this( StackIcon.ICON_BACK, img, zoom);
		mText = txt;
	}

	public Image getBack() {
		return mBack;
	}

	public int getIconHeight() {
		return mBack.getHeight( null);
	}

	public int getIconWidth() {
		return mBack.getWidth( null);
	}

	public void paintIcon( Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		AEntity.drawImage( g2, c, mBack);
		if (mImage != null) {
			AEntity.drawCenteredImage( g2, c, mImage, mZoom);
		}
		if (mText != null) {
			g2.setFont( AEntity.FONT_TITLE);
			g2.setColor( Color.BLACK);
			AEntity.drawBottomLeftString( g2, c, mText);
		}
	}
}
