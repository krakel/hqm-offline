package de.doerl.hqm.quest;

public enum LifeSetting {
	SHARE( "Shared lives", "Everyone puts their lives into a shared pool. If anyone dies a life is removed from there. Everyone needs at least one life to be kept in the game, so if your death results in the party getting too few lives, you get banned. The others can keep playing."),
	INDIVIDUAL( "Individual lives", "Everyone keeps their lives separated. If you run out of lives, you're out of the game. The other players in the party can continue playing with their lives.");
	private String mTitle;
	private String mDescr;

	private LifeSetting( String title, String desrc) {
		mTitle = title;
		mDescr = desrc;
	}

	public static LifeSetting get( int idx) {
		LifeSetting[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public static LifeSetting parse( String name) {
		if (name != null) {
			try {
				return valueOf( name);
			}
			catch (Exception ex) {
			}
		}
		return SHARE;
	}

	public String getDescr() {
		return mDescr;
	}

	public String getTitle() {
		return mTitle;
	}
}
