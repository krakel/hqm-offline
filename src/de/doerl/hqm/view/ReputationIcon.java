package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import javax.swing.Icon;

import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.utils.ResourceManager;

class ReputationIcon implements Icon {
	private Image mImage = ResourceManager.getImageUI( "hqm.reputation");
	private Image mMarker = ResourceManager.getImageUI( "hqm.marker");
	private Image mNeutral = ResourceManager.getImageUI( "hqm.neutral");
	private Image mCurrent = ResourceManager.getImageUI( "hqm.current");
	private FReputation mRep;

	public ReputationIcon( FReputation rep) {
		mRep = rep;
	}

	private void drawImage( Graphics2D g2, Image img, int x, int y) {
		AEntity.drawImage( g2, img, AEntity.ZOOM, AEntity.ZOOM, x + 2, y);
	}

	public int getIconHeight() {
		return 250;
	}

	public int getIconWidth() {
		return 22;
	}

	public void paintIcon( Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		drawImage( g2, mImage, -2, 5);
		if (mRep != null) {
			Vector<FMarker> marker = mRep.mMarker;
			int size = marker.size();
			if (size > 0) {
				FMarker first = marker.get( 0);
				FMarker last = marker.get( size - 1);
				int min = Math.min( first.mMark, 0);
				int max = Math.max( last.mMark, 0);
				int hub = max - min;
				if (hub > 0) {
					for (int i = 0; i < marker.size(); ++i) {
						FMarker mm = marker.get( i);
						int pos = 117 * (mm.mMark - min) / hub;
						drawImage( g2, mMarker, pos, 6);
					}
				}
				else if (hub == 0) {
					int pos = 117 / 2;
					drawImage( g2, mMarker, pos, 6);
				}
				else if (min <= 0 && 0 <= max) {
					int pos = 117 * (0 - min) / hub;
					drawImage( g2, mNeutral, pos, 6);
				}
				else if (min > 0) {
					int pos = 0;
					drawImage( g2, mCurrent, pos, 0);
				}
				else if (max < 0) {
					int pos = 117;
					drawImage( g2, mCurrent, pos, 0);
				}
				else {
					int pos = 117 * (0 - min) / hub;
					drawImage( g2, mCurrent, pos, 0);
				}
			}
		}
	}
}
