package de.doerl.hqm.quest;

public enum TriggerType {
	NONE( false, false, "Just a normal quest."),
	QUEST_TRIGGER( false, true, "A trigger quest is an invisible quest. The quest can still be completed as usual but you can't claim any rewards for it or see it in any lists. It can be used to trigger other quests, hence its name."),
	TASK_TRIGGER( true, true, "Trigger tasks are the first few tasks of a quest that have to be completed before the quest shows up. The quest will be invisible until the correct amount of tasks have been completed. When the quest becomes visible the player can see the tasks that have already been completed."),
	ANTI_TRIGGER( false, false, "This quest will be invisible until it is enabled (all its parent quests are completed). This way you can make a secret quest line appear all of a sudden when a known quest is completed.");
	private boolean mUseTaskCount;
	private boolean mWorkAsInvisible;
	private String mDesc;

	private TriggerType( boolean useTaskCount, boolean workAsInvisible, String desc) {
		mUseTaskCount = useTaskCount;
		mWorkAsInvisible = workAsInvisible;
		mDesc = desc;
	}

	public static TriggerType get( int idx) {
		TriggerType[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public static TriggerType parse( String name) {
		try {
			return valueOf( name);
		}
		catch (Exception ex) {
			return NONE;
		}
	}

	public String getDescription() {
		return mDesc;
	}

	public boolean isUseTaskCount() {
		return mUseTaskCount;
	}

	public boolean isWorkAsInvisible() {
		return mWorkAsInvisible;
	}
}
