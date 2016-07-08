package de.doerl.hqm.view.leafs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.AEntity;

public class LeafLine extends JPanel {
	private static final long serialVersionUID = -5876257198033732175L;
	private static final Color LINE_COLOR = new Color( 0xff404040);
	private static final int LINE_SIZE = 6;
	private int mDX, mDY;
	private Color mColor = LINE_COLOR;
	private FQuest mFrom;
	private FQuest mTo;

	public LeafLine( FQuest from, FQuest to) {
		mFrom = from;
		mTo = to;
		setOpaque( false);
		setBorder( null);
//		setBorder( BorderFactory.createLineBorder( Color.BLUE));
		int x1 = AEntity.ZOOM * from.getCenterX();
		int y1 = AEntity.ZOOM * from.getCenterY();
		int x2 = AEntity.ZOOM * to.getCenterX();
		int y2 = AEntity.ZOOM * to.getCenterY();
		updateBounds( x1, y1, x2, y2);
	}

	public boolean match( FQuest quest) {
		return mFrom.equals( quest) || mTo.equals( quest);
	}

	public boolean match( FQuest from, FQuest to) {
		return mFrom.equals( from) && mTo.equals( to);
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

	public void updateBounds( FQuest quest, int x, int y) {
		SwingUtilities.invokeLater( new UpdateBounds( quest, x, y));
	}

	private void updateBounds( int x1, int y1, int x2, int y2) {
		mDX = x1 - x2;
		mDY = y1 - y2;
		int x = Math.min( x1, x2);
		int y = Math.min( y1, y2) - LINE_SIZE / 2;
		int width = Math.max( Math.abs( mDX), LINE_SIZE);
		int height = Math.max( Math.abs( mDY), LINE_SIZE);
		setBounds( x, y, width, height);
	}

	private void updateBoundsFrom( int x, int y) {
		int x1 = x + AEntity.ZOOM * ResourceManager.getW5( mFrom.mBig);
		int y1 = y + AEntity.ZOOM * ResourceManager.getH5( mFrom.mBig);
		int x2 = AEntity.ZOOM * mTo.getCenterX();
		int y2 = AEntity.ZOOM * mTo.getCenterY();
		updateBounds( x1, y1, x2, y2);
	}

	private void updateBoundsTo( int x, int y) {
		int x1 = AEntity.ZOOM * mFrom.getCenterX();
		int y1 = AEntity.ZOOM * mFrom.getCenterY();
		int x2 = x + AEntity.ZOOM * ResourceManager.getW5( mTo.mBig);
		int y2 = y + AEntity.ZOOM * ResourceManager.getH5( mTo.mBig);
		updateBounds( x1, y1, x2, y2);
	}

	private final class UpdateBounds implements Runnable {
		private FQuest mQuest;
		private int mX;
		private int mY;

		private UpdateBounds( FQuest quest, int x, int y) {
			mQuest = quest;
			mX = x;
			mY = y;
		}

		@Override
		public void run() {
			if (Utils.equals( mQuest, mFrom)) {
				updateBoundsFrom( mX, mY);
			}
			if (Utils.equals( mQuest, mTo)) {
				updateBoundsTo( mX, mY);
			}
		}
	}
}
