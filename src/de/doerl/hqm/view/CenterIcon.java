package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

class CenterIcon implements Icon {
	private BufferedImage mImage;
	private String mText;

	public CenterIcon( BufferedImage img) {
		mImage = img;
	}

	public CenterIcon( BufferedImage img, String text) {
		mImage = img;
		mText = text;
	}

	public int getIconHeight() {
		return mImage.getWidth();
	}

	public int getIconWidth() {
		return mImage.getHeight();
	}

	public void paintIcon( Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		AEntity.drawImage( g2, c, mImage);
		if (mText != null) {
			g2.setFont( AEntity.FONT_NORMAL);
			g2.setColor( Color.BLACK);
			AEntity.drawCenteredString( g2, c, mText);
		}
	}
}
