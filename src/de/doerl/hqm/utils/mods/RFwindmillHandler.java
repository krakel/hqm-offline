package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

class RFwindmillHandler extends AHandler {
	private static HashMap<String, String> sNames;
	static {
		sNames = new HashMap<>();
	}

	@Override
	String getKey( String fileName) {
		String name = sNames.get( fileName);
		return name != null ? name : fileName;
	}

	@Override
	public String getName() {
		return "rfwindmill";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MOD_DIR);
	}

	@Override
	String getToken() {
		return "rf";
	}

	@Override
	boolean isBlock( String name) {
		return name.startsWith( "assets/rfwindmill/textures/blocks");
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/rfwindmill/textures/items");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "rfwindmill");
	}
}
