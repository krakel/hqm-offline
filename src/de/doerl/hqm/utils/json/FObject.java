package de.doerl.hqm.utils.json;

import java.util.HashMap;

public final class FObject implements IJson {
	private HashMap<String, IJson> mMap = new HashMap<>();

	FObject() {
	}

	public static FObject to( IJson json) {
		if (json instanceof FObject) {
			return (FObject) json;
		}
		else {
			return null;
		}
	}

	public IJson get( String key) {
		return mMap.get( key);
	}

	void put( String key, IJson value) {
		mMap.put( key, value);
	}
}
