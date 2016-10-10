package de.doerl.hqm.quest;

public enum FileVersion {
	INITIAL( "INITIAL"),
	QUESTS( "QUESTS"),
	SETS( "SETS"),
	LORE( "LORE"),
	UNCOMPLETED_DISABLED( "UNCOMPLETED_DISABLED"),
	LORE_AUDIO( "LORE_AUDIO"),
	BAGS( "BAGS"),
	LOCK( "LOCK"),
	BAG_LIMITS( "BAG_LIMITS"),
	TEAMS( "TEAMS"),
	TEAM_SETTINGS( "TEAM_SETTINGS"),
	DEATHS( "DEATHS"),
	REMOVED_QUESTS( "REMOVED_QUESTS"),
	REPEATABLE_QUESTS( "REPEATABLE_QUESTS"),
	TRIGGER_QUESTS( "TRIGGER_QUESTS"),
	OPTION_LINKS( "OPTION_LINKS"),
	NO_ITEM_IDS( "NO_ITEM_IDS"),
	NO_ITEM_IDS_FIX( "NO_ITEM_IDS_FIX"),
	PARENT_COUNT( "PARENT_COUNT"),
	REPUTATION( "REPUTATION"),
	REPUTATION_KILL( "REPUTATION_KILL"),
	REPUTATION_BARS( "REPUTATION_BARS"),
	CUSTOM_PRECISION_TYPES( "CUSTOM_PRECISION_TYPES"),
	COMMAND_REWARDS( "COMMAND_REWARDS");
	private String mName;

	private FileVersion( String name) {
		mName = name;
	}

	public static FileVersion get( int idx) {
		FileVersion[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public static FileVersion last() {
		FileVersion[] values = values();
		return values[values.length - 1];
	}

	public static FileVersion parse( String name) {
		if (name != null) {
			try {
				return valueOf( name);
			}
			catch (Exception ex) {
			}
		}
		return last();
	}

	public boolean contains( FileVersion other) {
		return ordinal() >= other.ordinal();
	}

	public String getName() {
		return mName;
	}
}
