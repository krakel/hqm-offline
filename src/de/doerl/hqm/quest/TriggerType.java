package de.doerl.hqm.quest;

public enum TriggerType {
	NONE( false, false),
	QUEST_TRIGGER( false, true),
	TASK_TRIGGER( true, true),
	ANTI_TRIGGER( false, false);
	private boolean mUseTaskCount;
	private boolean mWorkAsInvisible;

	private TriggerType( boolean useTaskCount, boolean workAsInvisible) {
		mUseTaskCount = useTaskCount;
		mWorkAsInvisible = workAsInvisible;
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

	public boolean isUseTaskCount() {
		return mUseTaskCount;
	}

	public boolean isWorkAsInvisible() {
		return mWorkAsInvisible;
	}
}
