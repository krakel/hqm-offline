package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;

import de.doerl.hqm.ui.ADialog;

class ButtonIcon implements Icon {
	private Image mImage;
	private String mText;

	public ButtonIcon( Image img) {
		mImage = img;
	}

	public ButtonIcon( String text, Image img) {
		mImage = img;
		mText = text;
	}

	public int getIconHeight() {
		return mImage.getWidth( null);
	}

	public int getIconWidth() {
		return mImage.getHeight( null);
	}

	public void paintIcon( Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		AEntity.drawImage( g2, c, mImage);
		if (mText != null) {
			g2.setFont( ADialog.FONT_NORMAL);
			g2.setColor( Color.BLACK);
			AEntity.drawCenteredString( g2, c, mText);
		}
	}
}
