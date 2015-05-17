package de.doerl.hqm.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

class LeafLine extends JPanel {
	private static final long serialVersionUID = -5876257198033732175L;
	private int mDX, mDY;
	private int mWidth;
	private Color mColor;

	public LeafLine( int x1, int y1, int x2, int y2, int width, Color color) {
		mDX = x1 - x2;
		mDY = y1 - y2;
		mWidth = width;
		mColor = color;
		setOpaque( false);
		setBorder( null);
//		setBorder( BorderFactory.createLineBorder( Color.BLUE));
		int dw = width / 2;
		setBounds( Math.min( x1, x2), Math.min( y1, y2) - dw, Math.max( Math.abs( mDX), width), Math.max( Math.abs( mDY), width));
	}

	@Override
	protected void paintComponent( Graphics g) {
		if (ui != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor( mColor);
			g2.setStroke( new BasicStroke( mWidth));
			int dw = mWidth / 2;
			if (mDX > mWidth) {
				if (mDY > mWidth) {
					g2.drawLine( 0, 0, mDX, mDY);
				}
				else if (mDY < -mWidth) {
					g2.drawLine( 0, -mDY, mDX, 0);
				}
				else {
					g2.drawLine( 0, dw, mDX, dw);
				}
			}
			else if (mDX < -mWidth) {
				if (mDY > mWidth) {
					g2.drawLine( -mDX, 0, 0, mDY);
				}
				else if (mDY < -mWidth) {
					g2.drawLine( -mDX, -mDY, 0, 0);
				}
				else {
					g2.drawLine( 0, dw, -mDX, dw);
				}
			}
			else {
				g2.drawLine( dw, 0, dw, Math.abs( mDY));
			}
		}
	}
}
