package de.doerl.hqm.utils.nbt;

final class FList extends AList {
	private int mTag;

	FList( String name, int tag) {
		super( name, 9);
		mTag = tag;
	}

	@Override
	public int getTag() {
		return mTag;
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
