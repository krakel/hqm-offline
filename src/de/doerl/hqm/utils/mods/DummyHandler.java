package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.util.HashMap;

import de.doerl.hqm.utils.ResourceManager;

public class DummyHandler implements IHandler {
	private HashMap<String, Image> mCache = new HashMap<>();
	private String mName;
	private String mToken;

	public DummyHandler( String name) {
		mName = name;
		mToken = name.substring( 0, 2).toLowerCase();
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public Image load( String stk) {
		Image img = mCache.get( stk);
		if (img == null) {
			img = ResourceManager.stringImage( mToken + mCache.size());
			mCache.put( stk, img);
		}
		return img;
	}
}
