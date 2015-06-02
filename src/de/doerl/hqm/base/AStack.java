package de.doerl.hqm.base;

import de.doerl.hqm.utils.Nbt;

public abstract class AStack {
	AStack() {
	}

	public String countOf() {
		int count = getCount();
		return count > 1 ? Integer.toString( count) : null;
	}

	public abstract int getCount();

	public abstract int getDamage();

	public abstract String getKey();

	public abstract String getName();

	public abstract Nbt getNBT();
}
