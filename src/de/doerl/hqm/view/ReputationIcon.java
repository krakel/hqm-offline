package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.Icon;

import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

class ReputationIcon implements Icon {
	private FSetting mSetting;
	private BufferedImage mImage = ResourceManager.getImageUI( "hqm.reputation");

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
			lowerValue = Math.min( first.mMark.mValue, 0);
			lowerOnMarker = false;
		}
		else {
			lowerValue = lower.mMark.mValue;
			lowerOnMarker = Utils.equals( lower, first) && lower.mMark.mValue > 0;
			if (Utils.equals( lower.mName.mValue, rs.mRep.mNeutral.mValue) && last.mMark.mValue < 0) {
				lowerValue = last.mMark.mValue;
				lowerOnMarker = true;
//					lowerMovedInner = true;
//					lowerMoved = true;
			}
			else if (Utils.equals( lower, last)) {
				lowerOnMarker = true;
			}
			else if (lowerValue <= 0) {
				for (int i = 0; i < size; ++i) {
					if (marker.get( i).mMark.mValue >= lowerValue) {
						if (i > 0) {
							lowerValue = marker.get( i - 1).mMark.mValue;
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
		return mImage.getHeight();
	}

	public int getIconWidth() {
		return mImage.getWidth();
	}

	public void paintIcon( Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		AEntity.drawImage( g2, c, mImage); //, scale, scale, 0, 10);
		Vector<FMarker> marker = mSetting.mRep.mMarker;
		for (int i = 0; i < marker.size(); ++i) {
//				FMarker mark = marker.get( i);
			int pos = i * mImage.getWidth() / marker.size();
			AEntity.drawImage( g2, ResourceManager.getImageUI( "hqm.marker"), AEntity.ZOOM, AEntity.ZOOM, pos + 2, 12);
		}
	}
}
