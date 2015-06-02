package de.doerl.hqm.base;

import de.doerl.hqm.utils.Utils;

public abstract class AStack {
	private String mNBT;

	AStack( String nbt) {
		mNBT = nbt;
	}

	public String countOf() {
		int count = getCount();
		return count > 1 ? Integer.toString( count) : null;
	}

	public int getCount() {
		if (mNBT != null) {
			return Utils.parseInteger( getValue( "Count"), 1);
		}
		else {
			return 1;
		}
	}

	public int getDamage() {
		if (mNBT != null) {
			return Utils.parseInteger( getValue( "Damage"), 0);
		}
		else {
			return 0;
		}
	}

	public abstract String getKey();

	public String getName() {
		if (mNBT != null) {
			return getValue( "id");
		}
		else {
			return "unknown";
		}
	}

	public String getNBT() {
		return mNBT;
	}

	protected String getNbtStr() {
		if (mNBT != null) {
			return mNBT.toString();
		}
		else {
			return "null";
		}
	}

	protected String getValue( String key) {
		String text = mNBT.toString();
		int pos = text.indexOf( key);
		if (pos < 0) {
			return "";
		}
		int p1 = text.indexOf( '(', pos);
		if (p1 < 0) {
			return "";
		}
		int p2 = text.indexOf( ')', p1);
		if (p2 < 0) {
			return "";
		}
		return text.substring( p1 + 1, p2);
	}
}
