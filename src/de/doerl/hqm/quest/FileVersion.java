package de.doerl.hqm.quest;

public enum FileVersion {
	INITIAL,
	QUESTS,
	SETS,
	LORE,
	UNCOMPLETED_DISABLED,
	LORE_AUDIO,
	BAGS,
	LOCK,
	BAG_LIMITS,
	TEAMS,
	TEAM_SETTINGS,
	DEATHS,
	REMOVED_QUESTS,
	REPEATABLE_QUESTS,
	TRIGGER_QUESTS,
	OPTION_LINKS,
	NO_ITEM_IDS,
	NO_ITEM_IDS_FIX,
	PARENT_COUNT,
	REPUTATION,
	REPUTATION_KILL,
	REPUTATION_BARS,
	CUSTOM_PRECISION_TYPES,
	COMMAND_REWARDS;
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
}
