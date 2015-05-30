package de.doerl.hqm.utils.mods;

import java.util.HashMap;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

public class HardcoreHandler extends AHandler {
	private static HashMap<String, String> sNames;
	static {
		sNames = new HashMap<>();
		sNames.put( "quarterheart", "hearts%0");
		sNames.put( "halfheart", "hearts%1");
		sNames.put( "threequarts", "hearts%2");
		sNames.put( "heart", "hearts%3");
		sNames.put( "rottenheart", "hearts%4");
		sNames.put( "hqmQuestBook", "quest_book%0");
		sNames.put( "hqmQuestBookOp", "quest_book%1");
		sNames.put( "lootbag", "bags%0"); // GRAY
		sNames.put( "lootbag", "bags%1"); // GREEN
		sNames.put( "lootbag", "bags%2"); // BLUE
		sNames.put( "lootbag", "bags%3"); // ORANGE
		sNames.put( "lootbag", "bags%4"); // PURPLE
		sNames.put( "hqmInvalidItem", "hqmInvalidItem%0");
		//
		sNames.put( "hqmItemBarrel", "item_barrel%0");
//		sNames.put( "hqmItemBarrelEmpty", "apple%0");
		sNames.put( "hqmQuestPortal", "quest_portal%0");
//		sNames.put( "hqmQuestPortalMagic", "apple%0");
//		sNames.put( "hqmQuestPortalTech", "apple%0");
//		sNames.put( "hqmQuestPortalTechSide", "apple%0");
//		sNames.put( "hqmQuestPortalTechTop", "apple%0");
//		sNames.put( "hqmQuestPortalTransparent", "apple%0");
		sNames.put( "hqmQuestTracker", "quest_tracker%0");
	}

	@Override
	String getKey( String fileName) {
		String name = sNames.get( fileName);
		return name != null ? name : fileName;
	}

	@Override
	public String getName() {
		return "HardcoreQuesting";
	}

	@Override
	String getPath() {
		return PreferenceManager.getString( BaseDefaults.MOD_DIR);
	}

	@Override
	String getToken() {
		return "hq";
	}

	@Override
	boolean isBlock( String name) {
		return name.startsWith( "assets/hqm/textures/blocks");
	}

	@Override
	boolean isItem( String name) {
		return name.startsWith( "assets/hqm/textures/items");
	}

	@Override
	boolean isModFile( String name) {
		return name.startsWith( "HQM-The Journey");
	}
}
