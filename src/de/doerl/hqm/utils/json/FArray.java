package de.doerl.hqm.utils.json;

import java.util.ArrayList;
import java.util.Iterator;

public final class FArray implements IJson, Iterable<IJson> {
	private ArrayList<IJson> mList = new ArrayList<>();

	FArray() {
	}

	public static FArray to( IJson json) {
		if (json instanceof FArray) {
			return (FArray) json;
		}
		else {
			return null;
		}
	}

	void add( IJson json) {
		mList.add( json);
	}

	public IJson get( int idx) {
		return mList.get( idx);
	}

	@Override
	public Iterator<IJson> iterator() {
		return mList.iterator();
	}

	public int size() {
		return mList.size();
	}
}
