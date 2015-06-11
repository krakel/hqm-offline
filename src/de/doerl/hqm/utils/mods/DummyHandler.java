package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.util.Hashtable;

import de.doerl.hqm.utils.ResourceManager;

class DummyHandler implements IHandler {
	private Hashtable<String, Integer> mCounts = new Hashtable<>();

	public DummyHandler() {
	}

	@Override
	public String getName() {
		return "dummy";
	}

	@Override
	public Image load( String key) {
		String mod = key.substring( 0, 2).toLowerCase();
		Integer cnt = mCounts.get( mod);
		if (cnt == null) {
			cnt = Integer.valueOf( 1);
		}
		else {
			cnt = Integer.valueOf( cnt + 1);
		}
		mCounts.put( mod, cnt);
		return ResourceManager.stringImage( mod + cnt);
	}
}
