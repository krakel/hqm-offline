package de.doerl.hqm.utils.mods;

import java.util.ArrayList;

import de.doerl.hqm.utils.Utils;

public class Matcher {
	public String mKey;
	public String mFile;
	private String mLower;

	public Matcher( String key, String file) {
		mKey = key;
		mFile = file;
		mLower = file.toLowerCase();
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

	public String getName() {
		int pos = mKey.indexOf( '%');
		return mKey.substring( 0, pos);
	}
}
