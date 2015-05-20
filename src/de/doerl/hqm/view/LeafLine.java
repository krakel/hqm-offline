package de.doerl.hqm.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import de.doerl.hqm.base.FQuest;

class LeafLine extends JPanel {
	private static final long serialVersionUID = -5876257198033732175L;
	private static final Color LINE_COLOR = new Color( 0xff404040);
	private static final int LINE_SIZE = 6;
	private int mDX, mDY;
	private Color mColor = LINE_COLOR;

	public LeafLine( FQuest from, FQuest to) {
		int x1 = from.getCenterX();
		int y1 = from.getCenterY();
		int x2 = to.getCenterX();
		int y2 = to.getCenterY();
		init( x1, y1, x2, y2);
	}

	public LeafLine( int x1, int y1, int x2, int y2) {
		init( x1, y1, x2, y2);
	}

	private void init( int x1, int y1, int x2, int y2) {
		mDX = x1 - x2;
		mDY = y1 - y2;
		setOpaque( false);
		setBorder( null);
//		setBorder( BorderFactory.createLineBorder( Color.BLUE));
		int x = Math.min( x1, x2);
		int y = Math.min( y1, y2) - LINE_SIZE / 2;
		int width = Math.max( Math.abs( mDX), LINE_SIZE);
		int height = Math.max( Math.abs( mDY), LINE_SIZE);
		setBounds( x, y, width, height);
	}

	@Override
	protected void paintComponent( Graphics g) {
		if (ui != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor( mColor);
			g2.setStroke( new BasicStroke( LINE_SIZE));
			if (mDX > LINE_SIZE) {
				if (mDY > LINE_SIZE) {
					g2.drawLine( 0, 0, mDX, mDY);
				}
				else if (mDY < -LINE_SIZE) {
					g2.drawLine( 0, -mDY, mDX, 0);
				}
				else {
					g2.drawLine( 0, LINE_SIZE / 2, mDX, LINE_SIZE / 2);
				}
			}
			else if (mDX < -LINE_SIZE) {
				if (mDY > LINE_SIZE) {
					g2.drawLine( -mDX, 0, 0, mDY);
				}
				else if (mDY < -LINE_SIZE) {
					g2.drawLine( -mDX, -mDY, 0, 0);
				}
				else {
					g2.drawLine( 0, LINE_SIZE / 2, -mDX, LINE_SIZE / 2);
				}
			}
			else {
				g2.drawLine( LINE_SIZE / 2, 0, LINE_SIZE / 2, Math.abs( mDY));
			}
		}
	}
}
