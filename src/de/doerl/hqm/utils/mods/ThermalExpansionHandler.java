package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

class ThermalExpansionHandler extends AHandler {
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
		return "ThermalExpansion";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MODULE_DIR);
	}

	@Override
	String getToken() {
		return "te";
	}

	@Override
	boolean isBlock( String name) {
		return name.startsWith( "assets/thermalexpansion/textures/blocks");
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/thermalexpansion/textures/items");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "ThermalExpansion");
	}
}
