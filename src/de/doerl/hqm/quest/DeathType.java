package de.doerl.hqm.quest;

public enum DeathType {
	LAVA( "Lava"),
	FIRE( "Fire"),
	SUFFOCATION( "Suffocation"),
	THORNS( "Thorns"),
	DROWNING( "Drowning"),
	STARVATION( "Starvation"),
	FALL( "Fall"),
	VOID( "Void"),
	CRUSHED( "Crushed"),
	EXPLOSION( "Explosions"),
	MONSTER( "Monsters"),
	PLAYER( "Other players"),
	MAGIC( "Magic"),
	HQM( "Rotten Hearts"),
	OTHER( "Other / Unknown");
	private String mName;

	private DeathType( String name) {
		mName = name;
	}

	public static DeathType get( int idx) {
		DeathType[] values = values();
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

	public static DeathType parse( String name) {
		try {
			return valueOf( name);
		}
		catch (Exception ex) {
			return LAVA;
		}
	}

	public String getName() {
		return mName;
	}
}
