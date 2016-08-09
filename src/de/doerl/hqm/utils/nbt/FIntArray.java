package de.doerl.hqm.utils.nbt;

import java.util.ArrayList;
import java.util.Iterator;

import de.doerl.hqm.utils.Utils;

public final class FIntArray extends ANbt implements Iterable<Integer> {
	public static final int ID = 11;
	private ArrayList<Integer> mList = new ArrayList<>();

	FIntArray( String name) {
		super( name, ID);
	}

	public static FIntArray create( String name, int... values) {
		FIntArray res = new FIntArray( name);
		for (int i : values) {
			res.add( i);
		}
		return res;
	}

	void add( int value) {
		mList.add( value);
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
			FIntArray other = (FIntArray) obj;
			if (Utils.different( getName(), other.getName())) {
				return false;
			}
			if (Utils.different( mList, other.mList)) {
				return false;
			}
		}
		catch (ClassCastException ex) {
			return false;
		}
		return true;
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
	public void toString( StringBuilder sb) {
		boolean comma = false;
		sb.append( "INT-ARRAY(");
		for (Integer i : mList) {
			if (comma) {
				sb.append( ",");
			}
			sb.append( i);
			comma = true;
		}
		sb.append( ")");
	}
}
