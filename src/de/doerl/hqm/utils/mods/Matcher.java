package de.doerl.hqm.utils.mods;

import java.util.ArrayList;

public class Matcher {
	public ItemNEI mItem;

	public Matcher( ItemNEI item) {
		mItem = item;
	}

	public void findMatch( ArrayList<Matcher> arr, String value) {
		if (getItemNEI().mLower.contains( value)) {
			arr.add( this);
		}
	}

	public void findMatch1( ArrayList<ItemNEI> arr, String value) {
		getItemNEI().findItem( arr, value);
	}

	public ItemNEI getItemNEI() {
		return mItem;
	}
}
