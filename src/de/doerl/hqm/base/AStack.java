package de.doerl.hqm.base;

import de.doerl.hqm.utils.Utils;

public abstract class AStack {
	protected String mNBT;

	AStack( String nbt) {
		mNBT = nbt;
	}

	public String countOf() {
		int count = getCount();
		return count > 1 ? Integer.toString( count) : null;
	}

	public abstract int getCount();

	public abstract int getDamage();

	public abstract String getDisplay();

	public abstract String getKey();

	public abstract String getName();

	public abstract String getNBT();

	protected String getNbtStr() {
		if (mNBT != null) {
			return mNBT.toString();
		}
		else {
			return "null";
		}
	}

	private String getValue( String key) {
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

	protected String getValueID( String key, String def) {
		if (mNBT != null) {
			String str = getValue( key);
			return str != null ? str : def;
		}
		else {
			return def;
		}
	}

	protected int getValueInt( String key, int def) {
		if (mNBT != null) {
			return Utils.parseInteger( getValue( key), def);
		}
		else {
			return def;
		}
	}

	protected String getValueStr( String key, String def) {
		if (mNBT != null) {
			String str = getValue( key);
			if (str != null) {
				return str.substring( 1, str.length() - 1);
			}
			else {
				return def;
			}
		}
		else {
			return def;
		}
	}
}
