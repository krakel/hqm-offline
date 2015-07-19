package de.doerl.hqm.quest;

public enum RewardSetting {
	ALL( "Multiple rewards", "Everyone in the party can claim their rewards from a quest. This option gives more rewards than the others but can be disabled in the config."),
	ANY( "Shared rewards", "The party receives one set of rewards when completing a quest. Anyone can claim the reward but as soon as it is claimed no body else can claim it."),
	RANDOM( "Random rewards", "Each time the party completes a quest that set of rewards is assigned to a player. This player is the only one that can claim the rewards.");
	private String mTitle;
	private String mDescr;

	private RewardSetting( String title, String descr) {
		mTitle = title;
		mDescr = descr;
	}

	public static RewardSetting get( int idx) {
		RewardSetting[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public static RewardSetting getDefault() {
		return ALL; // isAllModeEnabled ? ALL : ANY;
	}

	public static RewardSetting parse( String name) {
		try {
			return valueOf( name);
		}
		catch (Exception ex) {
			return ALL;
		}
	}

	public String getDescr() {
		return mDescr;
	}

	public String getTitle() {
		return mTitle;
	}
}
