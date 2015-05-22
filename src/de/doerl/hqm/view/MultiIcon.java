package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

class MultiIcon implements Icon {
	private int mWidth, mHeight;
	private BufferedImage[] mArr;

	public MultiIcon( int w, int h, BufferedImage... arr) {
		mWidth = w;
		mHeight = h;
		mArr = arr;
	}

	public int getIconHeight() {
		return mHeight;
	}

	public int getIconWidth() {
		return mWidth;
	}

	public void paintIcon( Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		for (BufferedImage img : mArr) {
			if (img != null) {
				AEntity.drawImage( g2, c, img);
			}
		}
	}
}
