package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;

class StackIcon implements Icon {
	private Image mBack;
	private Image mImage;
	private String mText;

	public StackIcon( Image back, Image img) {
		this( null, back, img);
	}

	public StackIcon( String text, Image back, Image stk) {
		mText = text;
		mBack = back;
		mImage = stk;
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
			AEntity.drawCenteredImage( g2, c, mImage, 0.5);
		}
		if (mText != null) {
			g2.setFont( AEntity.FONT_TITLE);
			g2.setColor( Color.BLACK);
			AEntity.drawBottomLeftString( g2, c, mText);
		}
	}
}
