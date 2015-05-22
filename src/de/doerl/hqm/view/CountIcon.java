package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

class CountIcon implements Icon {
	private BufferedImage mBack;
	private BufferedImage mStack;
	private String mText;

	public CountIcon( String text, BufferedImage back, BufferedImage stk) {
		mText = text;
		mBack = back;
		mStack = stk;
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
		if (mStack != null) {
			AEntity.drawImage( g2, c, mStack);
		}
		if (mText != null) {
			g2.setFont( AEntity.FONT_TITLE);
			g2.setColor( Color.BLACK);
			AEntity.drawBottomLeftString( g2, c, mText);
		}
	}
}
