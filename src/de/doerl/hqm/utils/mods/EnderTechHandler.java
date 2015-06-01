package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

class EnderTechHandler extends AHandler {
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
		return "EnderTech";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MOD_DIR);
	}

	@Override
	String getToken() {
		return "et";
	}

	@Override
	boolean isBlock( String name) {
		return name.startsWith( "assets/endertech/textures/blocks");
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/endertech/textures/items");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "EnderTech");
	}
}
