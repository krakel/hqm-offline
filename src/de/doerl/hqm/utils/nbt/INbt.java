package de.doerl.hqm.utils.nbt;

interface INbt {
	String getName();

	int getTag();

	void toString( StringBuilder sb);
}
