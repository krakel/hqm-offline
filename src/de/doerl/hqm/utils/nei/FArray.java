package de.doerl.hqm.utils.nei;

import java.util.ArrayList;
import java.util.Iterator;

import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.Utils;

public final class FArray implements IJson, Iterable<IJson> {
	private ArrayList<IJson> mList = new ArrayList<>();
	private String mByteArr;

	public FArray() {
	}

	public static FArray to( IJson json) {
		if (json instanceof FArray) {
			return (FArray) json;
		}
		else {
			return null;
		}
	}

	public void add( IJson json) {
		mList.add( json);
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
			FArray other = (FArray) obj;
			if (!mList.equals( other.mList)) {
				return false;
			}
			if (Utils.different( mByteArr, other.mByteArr)) {
				return false;
			}
		}
		catch (ClassCastException ex) {
			return false;
		}
		return true;
	}

	public IJson get( int idx) {
		return mList.get( idx);
	}

	public String getByteArr() {
		return mByteArr;
	}

	@Override
	public Iterator<IJson> iterator() {
		return mList.iterator();
	}

	public void setByteArr( String val) {
		if (mList.size() == 0 && val != null && val.endsWith( "bytes")) {
			mByteArr = val;
		}
	}

	public int size() {
		return mList.size();
	}

	@Override
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "list", mList);
		return sb.toString();
	}
}
