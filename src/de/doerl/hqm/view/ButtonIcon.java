package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

class ButtonIcon implements Icon {
	private BufferedImage mImage;
	private String mText;

	public ButtonIcon( BufferedImage img) {
		mImage = img;
	}

	public ButtonIcon( String text, BufferedImage img) {
		mImage = img;
		mText = text;
	}

	private static void drawCenteredString( Graphics2D g2, Component c, String text) {
		FontMetrics fm = g2.getFontMetrics();
		int x = (c.getWidth() - fm.stringWidth( text)) / 2;
		int y = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
		g2.drawString( text, x, y);
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
			drawCenteredString( g2, c, mText);
		}
	}
}
