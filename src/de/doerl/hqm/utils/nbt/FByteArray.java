package de.doerl.hqm.utils.nbt;

import java.util.ArrayList;
import java.util.Iterator;

final class FByteArray extends ANbt implements Iterable<Byte> {
	private ArrayList<Byte> mList = new ArrayList<>();

	FByteArray( String name) {
		super( name, 7);
	}

	void add( int value) {
		mList.add( (byte) value);
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
