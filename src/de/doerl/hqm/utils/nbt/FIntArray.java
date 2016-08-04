package de.doerl.hqm.utils.nbt;

import java.util.ArrayList;
import java.util.Iterator;

final class FIntArray extends ANbt implements Iterable<Integer> {
	private ArrayList<Integer> mList = new ArrayList<>();

	FIntArray( String name) {
		super( name, 11);
	}

	void add( int value) {
		mList.add( value);
	}

	public int get( int idx) {
		return mList.get( idx);
	}

	@Override
	public Iterator<Integer> iterator() {
		return mList.iterator();
	}

	public int size() {
		return mList.size();
	}

	@Override
	public void toString( StringBuffer sb) {
		boolean comma = false;
		sb.append( "INT-ARRAY( ");
		for (Integer i : mList) {
			if (comma) {
				sb.append( ", ");
			}
			sb.append( i);
			comma = true;
		}
		sb.append( ")");
	}
}
