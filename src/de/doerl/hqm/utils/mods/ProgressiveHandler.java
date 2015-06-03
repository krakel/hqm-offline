package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

class ProgressiveHandler extends AHandler {
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
		return "progressiveautomation";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MODULE_DIR);
	}

	@Override
	String getToken() {
		return "pa";
	}

	@Override
	boolean isBlock( String name) {
		return name.startsWith( "assets/progressiveautomation/textures/blocks");
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/progressiveautomation/textures/items");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "ProgressiveAutomation");
	}
}
