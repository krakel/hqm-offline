package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;

import de.doerl.hqm.utils.ResourceManager;

class StackIcon implements Icon {
	public static final Image ICON_BACK = ResourceManager.getImageUI( "hqm.icon.back");
	public static final Image ICON_UNKNOWN = ResourceManager.getImageUI( "hqm.unknown");
	public static final Dimension ICON_SIZE = AEntity.sizeOf( ICON_BACK);
	private Image mBack;
	private Image mImage;
	private String mText;
	private double mZoom;

	public StackIcon() {
		this( ICON_BACK, ICON_UNKNOWN, 0.6, null);
	}

	public StackIcon( Image img) {
		this( ICON_BACK, img, 0.6, null);
	}

	public StackIcon( Image img, double zoom, String txt) {
		this( ICON_BACK, img, zoom, txt);
	}

	public StackIcon( Image back, Image img, double zoom) {
		this( back, img, zoom, null);
	}

	private StackIcon( Image back, Image img, double zoom, String txt) {
		mBack = back;
		mImage = img;
		mZoom = zoom;
		mText = txt;
	}

	public StackIcon( Image img, String txt) {
		this( ICON_BACK, img, 0.6, txt);
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
			g2.setFont( AEntity.FONT_STACK);
			g2.setColor( Color.BLACK);
			AEntity.drawBottomLeftString( g2, c, mText);
		}
	}
}
