package de.doerl.hqm.quest;

public enum RepeatType {
	NONE( false),
	INSTANT( false),
	INTERVAL( true),
	TIME( true);
	private boolean mUseTime;

	private RepeatType( boolean useTime) {
		mUseTime = useTime;
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
		try {
			return valueOf( name);
		}
		catch (Exception ex) {
			return NONE;
		}
	}

	public boolean isUseTime() {
		return mUseTime;
	}
}
