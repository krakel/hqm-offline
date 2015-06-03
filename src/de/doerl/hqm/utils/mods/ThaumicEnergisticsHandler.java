package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

class ThaumicEnergisticsHandler extends AHandler {
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
		return "thaumicenergistics";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MODULE_DIR);
	}

	@Override
	String getToken() {
		return "tn";
	}

	@Override
	boolean isBlock( String name) {
		return name.startsWith( "assets/thaumicenergistics/textures/blocks");
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/thaumicenergistics/textures/items");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "thaumicenergistics");
	}
}
