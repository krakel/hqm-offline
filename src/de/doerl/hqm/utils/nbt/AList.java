package de.doerl.hqm.utils.nbt;

import java.util.ArrayList;
import java.util.Iterator;

abstract class AList extends ANbt implements Iterable<ANbt> {
	protected ArrayList<ANbt> mList = new ArrayList<>();

	AList( String name, int tag) {
		super( name, tag);
	}

	void add( ANbt nbt) {
		mList.add( nbt);
	}

	public ANbt get( int idx) {
		return mList.get( idx);
	}

	@Override
	public Iterator<ANbt> iterator() {
		return mList.iterator();
	}

	public int size() {
		return mList.size();
	}
}
