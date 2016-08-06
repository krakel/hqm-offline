package de.doerl.hqm.utils.nbt;

import de.doerl.hqm.utils.Utils;

public final class FList extends AList {
	FList( String name) {
		super( name, 9);
	}

	public static FList create( String name, ANbt... values) {
		FList res = new FList( name);
		for (ANbt nbt : values) {
			res.add( nbt);
		}
		return res;
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
			FList other = (FList) obj;
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

	public int getElement() {
		return mList.size() > 0 ? mList.get( 0).getTag() : 0;
	}

	@Override
	public void toString( StringBuffer sb) {
		boolean comma = false;
		sb.append( "LIST( ");
		for (ANbt nbt : mList) {
			if (comma) {
				sb.append( ", ");
			}
			nbt.toString( sb);
			comma = true;
		}
		sb.append( ")");
	}
}
