package de.doerl.hqm.quest;

public enum RepeatType {
	NONE( null, "This quest is not repeatable and can therefore only be completed once."),
	INSTANT( "i", "As soon as this quest is completed it can be completed again for another set of rewards"),
	INTERVAL( "v", "At a specific interval this quest will be reset and available for completion again. The quest is only reset if it has already been completed."),
	TIME( "t", "After completing this quest it goes on a cooldown, when this cooldown ends you can complete the quest again.");
	private String mMark;
	private String mDesc;

	private RepeatType( String mark, String desc) {
		mMark = mark;
		mDesc = desc;
	}

	public static RepeatType get( int idx) {
		RepeatType[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public static RepeatType parse( String name) {
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

	public boolean isUseTime() {
		return INTERVAL == this || TIME == this;
	}
}
