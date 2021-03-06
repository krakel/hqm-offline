package de.doerl.hqm.utils.nbt;

import java.util.ArrayList;
import java.util.Iterator;

import de.doerl.hqm.utils.Utils;

public final class FByteArray extends ANbt implements Iterable<Byte> {
	private ArrayList<Byte> mList = new ArrayList<>();

	FByteArray( String name) {
		super( name, ANbt.ID_BYTE_ARRAY);
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

	@Override
	int matcher( ANbt other) {
		if (other != null && getTag() == other.getTag() && Utils.equals( getName(), other.getName())) {
			FByteArray o = (FByteArray) other;
			if (Utils.equals( mList, o.mList)) {
				return 1;
			}
		}
		return 0;
	}

	public int size() {
		return mList.size();
	}

	@Override
	public void toString( StringBuilder sb) {
		boolean comma = false;
		sb.append( '[');
		for (int i : mList) {
			if (comma) {
				sb.append( ',');
			}
			sb.append( i);
			sb.append( 'b');
			comma = true;
		}
		sb.append( ']');
	}
}
