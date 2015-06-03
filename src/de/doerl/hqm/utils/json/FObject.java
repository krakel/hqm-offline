package de.doerl.hqm.utils.json;

import java.util.HashMap;
import java.util.Iterator;

public final class FObject implements IJson, Iterable<String> {
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

	@Override
	public Iterator<String> iterator() {
		return mMap.keySet().iterator();
	}

	void put( String key, IJson value) {
		mMap.put( key, value);
	}
}
