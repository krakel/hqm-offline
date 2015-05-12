package de.doerl.hqm.quest;

public enum Visibility {
	FULL,
	LOCATION,
	NONE;
	public static Visibility get( int idx) {
		Visibility[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}
}
