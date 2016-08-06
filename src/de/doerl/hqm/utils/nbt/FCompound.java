package de.doerl.hqm.utils.nbt;

import de.doerl.hqm.utils.Utils;

public final class FCompound extends AList {
	FCompound( String name) {
		super( name, 10);
	}

	public static FCompound create() {
		return new FCompound( "");
	}

	public static FCompound create( ANbt... values) {
		FCompound res = new FCompound( "");
		for (ANbt nbt : values) {
			res.add( nbt);
		}
		return res;
	}

	public static FCompound create( String name, ANbt... values) {
		FCompound res = new FCompound( name);
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
			FCompound other = (FCompound) obj;
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

	public ANbt get( String name) {
		for (ANbt nbt : mList) {
			if (Utils.equals( name, nbt.getName())) {
				return nbt;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( getName());
		sb.append( "=");
		toString( sb);
		return sb.toString();
	}

	@Override
	public void toString( StringBuffer sb) {
		boolean comma = false;
		sb.append( "COMPOUND( ");
		for (ANbt nbt : mList) {
			if (comma) {
				sb.append( ", ");
			}
			sb.append( nbt.getName());
			sb.append( "=");
			nbt.toString( sb);
			comma = true;
		}
		sb.append( ")");
	}
}
