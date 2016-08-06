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

	public String findZtones( String base) {
		if (base.endsWith( "_")) {
			base = base.substring( 0, base.length() - 1);
			int i = 9450; // ⓪
			String name = base + (char) i;
			if (mNames.contains( name)) {
				i = 9312; // ① ... ⑳
				do {
					name = base + (char) i++;
				}
				while (mNames.contains( name));
			}
			mNames.add( name);
			return name;
		}
		else {
			return find( base);
		}
	}
}
