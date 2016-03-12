package de.doerl.hqm.quest;

public enum ElementTyp {
	HQM,
	QUEST,
	QUEST_SET,
	QUEST_SET_CAT,
	QUEST_TASK,
	REPUTATION,
	REPUTATION_BAR,
	REPUTATION_CAT,
	REPUTATION_MARKER,
	REPUTATION_REWARD,
	REPUTATION_SETTING,
	LOCATION,
	MOB,
	REPEAT_INFO,
	ITEM_REQUIREMENT,
	FLUID_REQUIREMENT,
	GROUP,
	GROUP_TIER,
	GROUP_TIER_CAT;
	public static ElementTyp get( int idx) {
		ElementTyp[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public static ElementTyp parse( String name) {
		try {
			return valueOf( name);
		}
		catch (Exception ex) {
			return null;
		}
	}
}
