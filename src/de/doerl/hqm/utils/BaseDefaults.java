package de.doerl.hqm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.json.FArray;
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.FValue;
import de.doerl.hqm.utils.json.IJson;
import de.doerl.hqm.utils.json.JsonReader;
import de.doerl.hqm.utils.json.JsonWriter;

public class BaseDefaults {
	private static final Logger LOGGER = Logger.getLogger( BaseDefaults.class.getName());
	public static final String STACKTRC = "stackTrace.enabled";
	public static final String LAST_OPEN = "last.open";
	public static final int LAST_OPEN_MAX = 16;
	public static final String LAST_OPEN_DIR = "last.open.dir";
	public static final String FILE_OPEN_DIR = "file.open.directory";
	public static final String MINECRAFT_DIR = "minecraft.dir";
	public static final String MODULE_DIR = "mod.dir";
	public static final String DUMP_DIR = "dump.dir";
	public static final String FILE_VERSION = "version";
	private static final String[] KEYS = {
		LAST_OPEN, LAST_OPEN_DIR, FILE_OPEN_DIR, STACKTRC, MINECRAFT_DIR, MODULE_DIR, DUMP_DIR, FILE_VERSION
	};
	// @formatter:off
	private static final Object[][] DEFAULTS = {
		{ STACKTRC, Boolean.TRUE },
//		{ MINECRAFT_DIR, System.getProperty( "user.home") },
//		{ MOD_DIR, System.getProperty( "user.home") },
		{ FILE_VERSION, FileVersion.last().toString() },
		{ FILE_OPEN_DIR, System.getProperty( "user.home") },
		{ LAST_OPEN, new String[0] },
		{ LAST_OPEN_DIR, System.getProperty( "user.home") }
	};
	// @formatter:on
	private BaseDefaults() {
	}

	private static void check( PreferenceHash pref) {
		synchronized (pref) {
			checkBool( pref, STACKTRC);
			checkString( pref, MINECRAFT_DIR);
			checkString( pref, MODULE_DIR);
			checkString( pref, DUMP_DIR);
			checkString( pref, FILE_VERSION);
			checkString( pref, FILE_OPEN_DIR);
			checkArray( pref, LAST_OPEN);
			checkArray( pref, LAST_OPEN_DIR);
		}
	}

	static void checkArray( PreferenceHash pref, String key) {
		if (!pref.containsKey( key)) {
			pref.setArray( key, getDefaultArray( key));
		}
	}

	static void checkBool( PreferenceHash pref, String key) {
		boolean def = getDefaultBoolean( key);
		if (pref.containsKey( key)) {
			def = pref.getBool( key, def); // correct 1,0 to true,false
		}
		pref.setBool( key, def);
	}

	static void checkInteger( PreferenceHash pref, String key) {
		if (!pref.containsKey( key)) {
			pref.setInt( key, getDefaultInteger( key));
		}
	}

	static void checkMaximum( PreferenceHash pref, String key, int max, String msg) {
		int val = pref.getInt( key);
		if (val > max) {
			pref.setInt( key, max);
			Utils.log( LOGGER, Level.INFO, msg);
		}
	}

	static void checkMinimum( PreferenceHash pref, String key, int min, String msg) {
		int val = pref.getInt( key);
		if (val < min) {
			pref.setInt( key, min);
			Utils.log( LOGGER, Level.INFO, msg);
		}
	}

	static void checkSelect( PreferenceHash pref, String key) {
		String[] def = getDefaultArray( key);
		String val = pref.getString( key, null);
		if (val != null) {
			for (int i = 0; i < def.length; ++i) {
				if (Utils.equals( val, def[i])) {
					return;
				}
			}
		}
		pref.setString( key, def.length > 0 ? def[0] : null);
	}

	static void checkString( PreferenceHash pref, String key) {
		if (!pref.containsKey( key)) {
			pref.setString( key, getDefaultString( key));
		}
	}

	public static Object getDefault( String key) {
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

	static void hashLoad( FileInputStream in, PreferenceHash prefs) throws IOException {
		JsonReader rdr = new JsonReader( in);
		IJson mJson = rdr.doAll();
		FObject obj = FObject.to( mJson);
		if (obj != null) {
			for (String key : obj) {
				IJson val = obj.get( key);
				FArray arr = FArray.to( val);
				if (arr != null) {
					int size = arr.size();
					String[] res = new String[size];
					for (int i = 0; i < size; ++i) {
						res[i] = FValue.toString( arr.get( i));
					}
					prefs.setArray( key, res);
					continue;
				}
				String s = FValue.toString( val);
				if (s != null) {
					if (Utils.isBooleanString( s)) {
						prefs.setBool( key, Boolean.parseBoolean( s));
						continue;
					}
					if (Utils.isIntegerString( s)) {
						prefs.setInt( key, Utils.parseInteger( s));
						continue;
					}
					prefs.setString( key, s);
				}
				else {
					Utils.log( LOGGER, Level.WARNING, "wrong preference key {0}", key);
				}
			}
		}
		check( prefs);
	}

	static void hashStore( OutputStream out, PreferenceHash prefs) throws IOException {
		JsonWriter dst = new JsonWriter( out);
		dst.beginObject();
		dst.print( "comment", "Generated by HQM Manager");
		dst.print( "time", new Date().toString());
		for (String key : KEYS) {
			Object obj = prefs.get( key);
			if (obj instanceof String[]) {
				dst.printArr( key, PreferenceHash.toArray( obj));
			}
			else if (obj instanceof Boolean) {
				dst.print( key, PreferenceHash.toBool( obj));
			}
			else if (obj instanceof Integer) {
				dst.print( key, PreferenceHash.toInt( key));
			}
			else if (obj != null) {
				dst.print( key, obj);
			}
		}
		dst.endObject();
		dst.flush();
	}
}
