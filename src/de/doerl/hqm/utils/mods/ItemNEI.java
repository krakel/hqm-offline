/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
package de.doerl.hqm.utils.mods;

import java.util.ArrayList;

import de.doerl.hqm.utils.Utils;

public class ItemNEI {
	public String mPkg;
	public String mName;
	public String mID;
	public int mDamage;
	public boolean mHasNBT;
	public String mBase;
	public String mImage;
	public String mLower;
	public String mKey;
	public String mNBT;

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
		if (mBase.length() > 0 && mBase.charAt( 0) == '"') {
			mBase = mBase.substring( 1, mBase.length() - 1);
		}
		mImage = mBase;
		mLower = mBase.toLowerCase();
		mKey = mName + '%' + mDamage;
		int p5 = mName.indexOf( ':');
		if (p5 > 0) {
			int p6 = mName.indexOf( '|');
			mPkg = mName.substring( 0, p6 < 0 ? p5 : p6);
		}
	}

	public void findImage( NameCache cache) {
		mImage = cache.find( mBase);
		mLower = mImage.toLowerCase();
	}

	public void findItem( ArrayList<ItemNEI> arr, String value) {
		if (mLower.contains( value)) {
			arr.add( this);
		}
	}

	public String getMod() {
		return mPkg != null ? mPkg : "unknown";
	}
}
