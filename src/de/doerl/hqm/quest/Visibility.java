package de.doerl.hqm.quest;

public enum Visibility {
	FULL( '+'),
	LOCATION( '~'),
	NONE( '-');
	private char mSymbol;

	private Visibility( char symbol) {
		mSymbol = symbol;
	}

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

	public static Visibility parse( String name) {
		try {
			return valueOf( name);
		}
		catch (Exception ex) {
			return FULL;
		}
	}

	public char getSymbol() {
		return mSymbol;
	}
}
