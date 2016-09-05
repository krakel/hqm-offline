package de.doerl.hqm.base;

import de.doerl.hqm.utils.nbt.FCompound;

public abstract class AStack {
	public abstract int getDamage();

	public abstract String getDisplay();

	public abstract String getKey();

	public abstract String getName();

	public abstract FCompound getNBT();

	public abstract int getStackSize();
}
