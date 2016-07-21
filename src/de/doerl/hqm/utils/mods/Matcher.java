package de.doerl.hqm.utils.mods;

import java.util.ArrayList;

import de.doerl.hqm.utils.Utils;

public class Matcher {
	public String mKey;
	public String mImage;
	public String mNBT;
	private String mLower;

	public Matcher( String key, String image) {
		mKey = key;
		mImage = image;
		mLower = image.toLowerCase();
	}

	public void findMatch( ArrayList<Matcher> arr, String value) {
		if (mLower.contains( value)) {
			arr.add( this);
		}
	}

	public int getDamage() {
		int pos = mKey.indexOf( '%');
		return Utils.parseInteger( mKey.substring( pos + 1), 0);
	}

	public String getMod() {
		int pos = mKey.indexOf( ':');
		return pos > 0 ? mKey.substring( 0, pos) : "unknown";
	}

	public String getName() {
		int pos = mKey.indexOf( '%');
		return mKey.substring( 0, pos);
	}
}
