package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

class BotaniaHandler extends AHandler {
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
		return "Botania";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MODULE_DIR);
	}

	@Override
	String getToken() {
		return "bo";
	}

	@Override
	boolean isBlock( String name) {
		return name.startsWith( "assets/botania/textures/blocks");
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/botania/textures/items");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "Botania");
	}
}
