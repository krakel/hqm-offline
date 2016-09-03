package de.doerl.hqm.quest;

public enum TriggerType {
	NONE( null, "Just a normal quest."),
	QUEST_TRIGGER( "Q", "A trigger quest is an invisible quest. The quest can still be completed as usual but you can't claim any rewards for it or see it in any lists. It can be used to trigger other quests, hence its name."),
	TASK_TRIGGER( "T", "Trigger tasks are the first few tasks of a quest that have to be completed before the quest shows up. The quest will be invisible until the correct amount of tasks have been completed. When the quest becomes visible the player can see the tasks that have already been completed."),
	ANTI_TRIGGER( "A", "This quest will be invisible until it is enabled (all its parent quests are completed). This way you can make a secret quest line appear all of a sudden when a known quest is completed.");
	private String mMark;
	private String mDesc;

	private TriggerType( String mark, String desc) {
		mMark = mark;
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
		if (name != null) {
			try {
				return valueOf( name);
			}
			catch (Exception ex) {
			}
		}
		return NONE;
	}

	public String getDescription() {
		return mDesc;
	}

	public String getMarker() {
		return mMark;
	}

	public boolean isUseTaskCount() {
		return TASK_TRIGGER == this;
	}
}
