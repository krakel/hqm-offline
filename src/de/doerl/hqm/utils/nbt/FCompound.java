package de.doerl.hqm.utils.nbt;

import de.doerl.hqm.utils.Utils;

final class FCompound extends AList {
	FCompound( String name) {
		super( name, 10);
	}

	ANbt get( String name) {
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
