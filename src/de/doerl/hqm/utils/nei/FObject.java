package de.doerl.hqm.utils.nei;

import java.util.HashMap;
import java.util.Iterator;

import de.doerl.hqm.utils.ToString;

public final class FObject implements IJson, Iterable<String> {
	private HashMap<String, IJson> mMap = new HashMap<>();

	public FObject() {
	}

	public static FObject to( IJson json) {
		if (json instanceof FObject) {
			return (FObject) json;
		}
		else {
			return null;
		}
	}

	@Override
	public boolean equals( Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		try {
			FObject other = (FObject) obj;
			if (!mMap.equals( other.mMap)) {
				return false;
			}
		}
		catch (ClassCastException ex) {
			return false;
		}
		return true;
	}

	public IJson get( String key) {
		return mMap.get( key);
	}

	@Override
	public Iterator<String> iterator() {
		return mMap.keySet().iterator();
	}

	public void put( String key, IJson value) {
		mMap.put( key, value);
	}

	@Override
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "map", mMap);
		return sb.toString();
	}
}
