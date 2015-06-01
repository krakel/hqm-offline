package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

class ForbiddenMagicHandler extends AHandler {
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
		return "ForbiddenMagic";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MOD_DIR);
	}

	@Override
	String getToken() {
		return "fm";
	}

	@Override
	boolean isBlock( String name) {
		return name.startsWith( "assets/forbidden/textures/blocks");
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/forbidden/textures/items");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "Forbidden Magic");
	}
}
