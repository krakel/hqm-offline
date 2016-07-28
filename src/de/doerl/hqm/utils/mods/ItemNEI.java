/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
package de.doerl.hqm.utils.mods;

import java.util.ArrayList;

import de.doerl.hqm.utils.Utils;

public class ItemNEI {
	public final String mName;
	public final String mKey;
	public final int mDamage;
	final String mID;
	final String mBase;
	final boolean mHasNBT;
	private String mPkg;
	private String mDisplay;
	private String mLower;
	private String mNBT;

	ItemNEI( String line) {
		int p1 = line.indexOf( ',');
		int p2 = line.indexOf( ',', p1 + 1);
		int p3 = line.indexOf( ',', p2 + 1);
		int p4 = line.indexOf( ',', p3 + 1);
		mName = line.substring( 0, p1);
		mID = line.substring( p1 + 1, p2);
		mDamage = Utils.parseInteger( line.substring( p2 + 1, p3), 0);
		mHasNBT = Utils.parseBoolean( line.substring( p3 + 1, p4), false);
		mBase = Utils.toWindowsName( line.substring( p4 + 1));
		mDisplay = mBase;
		mLower = mDisplay.toLowerCase();
		mKey = mName + '%' + mDamage;
		int p5 = mName.indexOf( ':');
		if (p5 > 0) {
			int p6 = mName.indexOf( '|');
			mPkg = mName.substring( 0, p6 < 0 ? p5 : p6);
		}
	}

	void findImage( NameCache cache) {
		mDisplay = cache.find( mBase);
		mLower = mDisplay.toLowerCase();
	}

	void findItem( ArrayList<ItemNEI> arr, String value) {
		if (mLower.contains( value)) {
			arr.add( this);
		}
	}

	String getImageName() {
		return mDisplay + ".png";
	}

	public String getMod() {
		return mPkg != null ? mPkg : "unknown";
	}

	public String getNBT() {
		return mNBT;
	}

	public String getPkg() {
		return mPkg;
	}
}
