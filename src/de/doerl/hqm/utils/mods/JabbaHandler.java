package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

class JabbaHandler extends AHandler {
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
		return "JABBA";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MOD_DIR);
	}

	@Override
	String getToken() {
		return "ja";
	}

	@Override
	boolean isBlock( String name) {
		return name.startsWith( "assets/jabba/textures/blocks");
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/jabba/textures/items");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "Jabba");
	}
}
