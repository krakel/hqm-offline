package de.doerl.hqm.utils.nbt;

final class FList extends AList {
	private int mElement;

	FList( String name, int tag) {
		super( name, 9);
		mElement = tag;
	}

	static FList create( String name, ANbt[] values) {
		int tag = values.length > 0 ? values[0].getTag() : 10;
		FList res = new FList( name, tag);
		for (ANbt nbt : values) {
			res.add( nbt);
		}
		return res;
	}

	public int getElement() {
		return mElement;
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
