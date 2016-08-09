package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.util.ArrayList;

import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.FCompound;

public class ItemNEI {
	private static final char[] NO_WINDOW_CHAR = "<>:\"/\\|?*".toCharArray();
	private static final char[] WINDOW_CHAR = new char[256];
	static {
		for (int i = 0; i < 256; ++i) {
			WINDOW_CHAR[i] = (char) i;
		}
		for (int i = 0; i < NO_WINDOW_CHAR.length; ++i) {
			WINDOW_CHAR[NO_WINDOW_CHAR[i]] = '_';
		}
	}
	public final String mName;
	public final String mKey;
	public final int mDamage;
	final String mID;
	final String mBase;
	final boolean mHasNBT;
	private String mPkg;
	private String mMod;
	private String mDisplay;
	private String mLower;
	private FCompound mNBT;
	private Image mImage;

	ItemNEI( String line) {
		int p1 = line.indexOf( ',');
		int p2 = line.indexOf( ',', p1 + 1);
		int p3 = line.indexOf( ',', p2 + 1);
		int p4 = line.indexOf( ',', p3 + 1);
		mName = line.substring( 0, p1);
		setPkg();
		mID = line.substring( p1 + 1, p2);
		mDamage = Utils.parseInteger( line.substring( p2 + 1, p3), 0);
		mHasNBT = Utils.parseBoolean( line.substring( p3 + 1, p4), false);
		mBase = toWindowsName( line.substring( p4 + 1));
		mDisplay = mBase;
		mLower = mDisplay.toLowerCase();
		mKey = mName + '%' + mDamage;
	}

	ItemNEI( String key, Object obj) {
		int p1 = key.indexOf( '%');
		mName = key.substring( 0, p1);
		setPkg();
		mID = null;
		mDamage = Utils.parseInteger( key.substring( p1 + 1), 0);
		mHasNBT = false;
		mBase = mName;
		mDisplay = "";
		mLower = mName.toLowerCase();
		mKey = key;
	}

	private static String toWindowsName( String value) {
		if (value.length() > 0 && value.charAt( 0) == '"') {
			value = value.substring( 1, value.length() - 1);
		}
		value = value.replace( "\"\"", "_");
		StringBuilder sb = new StringBuilder();
		for (int i = 0, max = value.length(); i < max; ++i) {
			char ch = value.charAt( i);
			sb.append( WINDOW_CHAR[ch]);
		}
		// value.replace( '<', '_').replace( '>', '_').replace( ':', '_').replace( '|', '_').replace( '/', '_').replace( '\\', '_').replace( '?', '_').replace( '*', '_').replace( '"', '_');
		return sb.toString();
	}

	void findImage( NameCache cache) {
		if ("Ztones".equals( mMod)) {
			mDisplay = cache.findZtones( mBase);
		}
		else {
			mDisplay = cache.find( mBase);
		}
		mLower = mDisplay.toLowerCase();
	}

	void findItem( ArrayList<ItemNEI> arr, String value) {
		if (mLower.contains( value)) {
			arr.add( this);
		}
	}

	public String getDisplay() {
		return mDisplay;
	}

	public Image getImage() {
		return mImage;
	}

	String getImageName() {
		return mDisplay + ".png";
	}

	public String getMod() {
		return mMod;
	}

	public FCompound getNBT() {
		return mNBT;
	}

	public String getPkg() {
		return mPkg;
	}

	public void setImage( Image image) {
		mImage = image;
	}

	public void setNBT( FCompound nBT) {
		mNBT = nBT;
	}

	private void setPkg() {
		int p1 = mName.indexOf( ':');
		if (p1 > 0) {
			int p2 = mName.indexOf( '|');
			mPkg = mName.substring( 0, p2 < 0 ? p1 : p2);
			mMod = mName.substring( 0, p1);
		}
		else {
			mMod = "unknown";
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( mName);
		sb.append( ",");
		sb.append( mID);
		sb.append( ",");
		sb.append( mDamage);
		sb.append( ",");
		sb.append( mBase);
		return sb.toString();
	}
}
