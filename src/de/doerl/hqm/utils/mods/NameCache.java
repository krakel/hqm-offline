/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
package de.doerl.hqm.utils.mods;

import java.util.HashSet;

class NameCache {
	private HashSet<String> mNames = new HashSet<>();

	public boolean checkImage( String name) {
		if (mNames.contains( name)) {
			return true;
		}
		mNames.add( name);
		return false;
	}

	public String find( String base) {
		String name = base;
		int i = 1;
		while (mNames.contains( name)) {
			name = base + '_' + ++i;
		}
		mNames.add( name);
		return name;
	}
}
