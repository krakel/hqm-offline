package de.doerl.hqm.questX.minecraft;

public class Item {
	String mName;

	public Item( int i) {
		mName = Integer.toString( i);
	}

	public Item( String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	@Override
	public String toString() {
		return mName;
	}
}
