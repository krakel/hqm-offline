package de.doerl.hqm.utils;

import java.awt.Color;

public class BaseDefaults {
//	private static final Logger LOGGER = Logger.getLogger( BaseDefaults.class.getName());
	public static final String FILE_OPEN_DIR = "file.open.directory";
	public static final String LAST_OPEN = "last.open";
	public static final String LAST_OPEN_DIR = "last.open.dir";
	public static final String STACKTRC = "stackTrace.enabled";
	public static final String MINECRAFT_DIR = "minecraft.dir";
	public static final String MOD_DIR = "mod.dir";
	private static final String[] KEYS = {
		FILE_OPEN_DIR, LAST_OPEN, LAST_OPEN_DIR, STACKTRC, MINECRAFT_DIR, MOD_DIR
	};
	// @formatter:off
	private static final Object[][] DEFAULTS = {
		{ STACKTRC, Boolean.TRUE },
//		{ MINECRAFT_DIR, System.getProperty( "user.home") },
//		{ MOD_DIR, System.getProperty( "user.home") },
		{ FILE_OPEN_DIR, System.getProperty( "user.home") },
		{ LAST_OPEN, new String[0] },
		{ LAST_OPEN_DIR, new String[0] }
	};
	// @formatter:on
	private BaseDefaults() {
	}

	static void check( PreferenceHash pref) {
		pref.checkBool( STACKTRC);
		pref.checkString( MINECRAFT_DIR);
		pref.checkString( MOD_DIR);
		pref.checkString( FILE_OPEN_DIR);
		pref.checkArray( LAST_OPEN);
		pref.checkArray( LAST_OPEN_DIR);
	}

	private static Object getDefault( String key) {
		for (int i = 0; i < DEFAULTS.length; ++i) {
			if (key.equals( DEFAULTS[i][0])) {
				return DEFAULTS[i][1];
			}
		}
		return null;
	}

	public static String[] getDefaultArray( String key) {
		try {
			Object obj = getDefault( key);
			if (obj != null) {
				return (String[]) obj;
			}
		}
		catch (RuntimeException ex) {
		}
		return new String[0];
	}

	public static boolean getDefaultBoolean( String key) {
		try {
			Object obj = getDefault( key);
			if (obj != null) {
				return ((Boolean) obj).booleanValue();
			}
		}
		catch (RuntimeException ex) {
		}
		return false;
	}

	public static Color getDefaultColor( String key) {
		try {
			Object obj = getDefault( key);
			if (obj != null) {
				return (Color) obj;
			}
		}
		catch (RuntimeException ex) {
		}
		return Color.BLACK;
	}

	public static int getDefaultInteger( String key) {
		try {
			Object obj = getDefault( key);
			if (obj != null) {
				return ((Integer) obj).intValue();
			}
		}
		catch (RuntimeException ex) {
		}
		return 0;
	}

	public static String getDefaultString( String key) {
		try {
			Object obj = getDefault( key);
			if (obj != null) {
				return obj.toString();
			}
		}
		catch (RuntimeException ex) {
		}
		return null;
	}

	static boolean isKeyForSave( String key) {
		for (int i = 0; i < KEYS.length; ++i) {
			if (key.startsWith( KEYS[i])) {
				return true;
			}
		}
		return false;
	}

	static void keys( PreferenceHash pref) {
		for (int i = 0; i < KEYS.length; ++i) {
			pref.setSave( KEYS[i], true);
		}
	}
}
