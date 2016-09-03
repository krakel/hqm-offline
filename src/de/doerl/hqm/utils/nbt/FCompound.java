package de.doerl.hqm.utils.nbt;

import de.doerl.hqm.utils.Utils;

public final class FCompound extends AList {
	FCompound( String name) {
		super( name, ANbt.ID_COMPOUND);
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

	public void clearName() {
		setName( "");
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

	public int match( FCompound other) {
		return matcher( other);
	}

	@Override
	int matcher( ANbt other) {
		int res = 0;
		if (other != null && getTag() == other.getTag() && Utils.equals( getName(), other.getName())) {
			FCompound ol = (FCompound) other;
			for (ANbt nbt : mList) {
				ANbt o = ol.get( nbt.getName());
				res += nbt.matcher( o);
			}
		}
		return res;
	}

	@Override
	public void toString( StringBuilder sb) {
		boolean comma = false;
		sb.append( "COMPOUND(");
		for (ANbt nbt : mList) {
			if (comma) {
				sb.append( ',');
			}
			sb.append( nbt.getName());
			sb.append( '=');
			nbt.toString( sb);
			comma = true;
		}
		sb.append( ')');
	}
}
