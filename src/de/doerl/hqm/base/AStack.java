package de.doerl.hqm.base;

import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.FLong;
import de.doerl.hqm.utils.nbt.FString;

public abstract class AStack {
	protected FCompound mNBT;

	AStack( FCompound nbt) {
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

	public abstract FCompound getNBT();

	public String getNbtStr() {
		if (mNBT != null) {
			return mNBT.toString();
		}
		else {
			return "";
		}
	}

	protected String getValueID( String key, String def) {
		if (mNBT != null) {
			return FString.to( mNBT.get( key), def);
		}
		else {
			return def;
		}
	}

	protected int getValueInt( String key, int def) {
		if (mNBT != null) {
			return FLong.toInt( mNBT.get( key), def);
		}
		else {
			return def;
		}
	}

	protected String getValueStr( String key, String def) {
		if (mNBT != null) {
			return FString.to( mNBT.get( key), def);
		}
		else {
			return def;
		}
	}
}
