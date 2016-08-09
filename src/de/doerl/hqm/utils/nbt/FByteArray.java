package de.doerl.hqm.utils.nbt;

import java.util.ArrayList;
import java.util.Iterator;

import de.doerl.hqm.utils.Utils;

public final class FByteArray extends ANbt implements Iterable<Byte> {
	public static final int ID = 7;
	private ArrayList<Byte> mList = new ArrayList<>();

	FByteArray( String name) {
		super( name, ID);
	}

	public static FByteArray create( String name, int... values) {
		FByteArray res = new FByteArray( name);
		for (int b : values) {
			res.add( b & 0xFF);
		}
		return res;
	}

	void add( int value) {
		mList.add( (byte) value);
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
			FByteArray other = (FByteArray) obj;
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

	public byte get( int idx) {
		return mList.get( idx);
	}

	@Override
	public Iterator<Byte> iterator() {
		return mList.iterator();
	}

	public int size() {
		return mList.size();
	}

	@Override
	public void toString( StringBuffer sb) {
		boolean comma = false;
		sb.append( "BYTE-ARRAY( ");
		for (Byte i : mList) {
			if (comma) {
				sb.append( ", ");
			}
			sb.append( i);
			comma = true;
		}
		sb.append( ")");
	}
}
