package de.doerl.hqm.quest;

public enum BagTier {
	BASIC,
	GOOD,
	GREATER,
	EPIC,
	LEGENDARY;
	public static BagTier get( int idx) {
		BagTier[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public static int[] newArray() {
		return new int[values().length];
	}

	public static BagTier parse( String name) {
		try {
			return valueOf( name);
		}
		catch (Exception ex) {
			return BASIC;
		}
	}
}
