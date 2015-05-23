package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

class StackIcon implements Icon {
	private BufferedImage mBack;
	private BufferedImage mImage;
	private String mText;

	public StackIcon( BufferedImage back, BufferedImage img) {
		this( null, back, img);
	}

	public StackIcon( String text, BufferedImage back, BufferedImage stk) {
		mText = text;
		mBack = back;
		mImage = stk;
	}

	private static void drawBottomLeftString( Graphics2D g2, Component c, String text) {
		FontMetrics fm = g2.getFontMetrics();
		int x = c.getWidth() - fm.stringWidth( text);
		int y = c.getHeight() - fm.getDescent();
		g2.drawString( text, x, y);
	}

	public int getIconHeight() {
		return mBack.getHeight();
	}

	public int getIconWidth() {
		return mBack.getWidth();
	}

	public void paintIcon( Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		AEntity.drawImage( g2, c, mBack);
		if (mImage != null) {
			AEntity.drawImage( g2, c, mImage);
		}
		if (mText != null) {
			g2.setFont( AEntity.FONT_TITLE);
			g2.setColor( Color.BLACK);
			drawBottomLeftString( g2, c, mText);
		}
	}
}
