package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import javax.swing.Icon;

import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

class ReputationIcon implements Icon {
	private FSetting mSetting;
	private Image mImage = ResourceManager.getImageUI( "hqm.reputation");

	public ReputationIcon( FSetting rs) {
		mSetting = rs;
		FMarker lower = rs.mLower;
		Vector<FMarker> marker = rs.mRep.mMarker;
		int size = marker.size();
		FMarker first = marker.get( 0);
		FMarker last = marker.get( size - 1);
		int lowerValue;
		boolean lowerOnMarker;
		if (lower == null) {
			lowerValue = Math.min( first.mMark, 0);
			lowerOnMarker = false;
		}
		else {
			lowerValue = lower.mMark;
			lowerOnMarker = Utils.equals( lower, first) && lower.mMark > 0;
			if (Utils.equals( lower.mName, rs.mRep.mNeutral) && last.mMark < 0) {
				lowerValue = last.mMark;
				lowerOnMarker = true;
//					lowerMovedInner = true;
//					lowerMoved = true;
			}
			else if (Utils.equals( lower, last)) {
				lowerOnMarker = true;
			}
			else if (lowerValue <= 0) {
				for (int i = 0; i < size; ++i) {
					if (marker.get( i).mMark >= lowerValue) {
						if (i > 0) {
							lowerValue = marker.get( i - 1).mMark;
							if (i - 1 != 0) {
//									lowerMovedInner = true;
							}
//								lowerMoved = true;
						}
						break;
					}
				}
			}
		}
//			FReputationMarker upper = rs.mUpper;
//			FReputation rep = rs.mRep;
	}

	public int getIconHeight() {
		return mImage.getHeight( null);
	}

	public int getIconWidth() {
		return mImage.getWidth( null);
	}

	public void paintIcon( Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		AEntity.drawImage( g2, c, mImage); //, scale, scale, 0, 10);
		Vector<FMarker> marker = mSetting.mRep.mMarker;
		for (int i = 0; i < marker.size(); ++i) {
//				FMarker mark = marker.get( i);
			int pos = i * mImage.getWidth( null) / marker.size();
			AEntity.drawImage( g2, ResourceManager.getImageUI( "hqm.marker"), AEntity.ZOOM, AEntity.ZOOM, pos + 2, 12);
		}
	}
}
