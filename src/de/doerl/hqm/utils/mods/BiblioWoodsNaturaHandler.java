package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

class BiblioWoodsNaturaHandler extends AHandler {
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
		return "BiblioWoodsNatura";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MOD_DIR);
	}

	@Override
	String getToken() {
		return "en";
	}

	@Override
	boolean isBlock( String name) {
		return false;
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/bibliocraft/textures/naturawoods");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "BiblioWoods[Natura]");
	}
}
