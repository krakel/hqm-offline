package de.doerl.hqm.utils.mods;

import java.util.ArrayList;

public class Matcher {
	public String mKey;
	public String mFile;

	public Matcher( String key, String file) {
		mKey = key;
		mFile = file;
	}

	public void findMatch( ArrayList<Matcher> arr, String value) {
		if (mFile.contains( value)) {
			arr.add( this);
		}
	}
}
