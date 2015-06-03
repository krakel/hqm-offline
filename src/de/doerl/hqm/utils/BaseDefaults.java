package de.doerl.hqm.utils;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.json.FArray;
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.FValue;
import de.doerl.hqm.utils.json.IJson;
import de.doerl.hqm.utils.json.JsonReader;
import de.doerl.hqm.utils.json.JsonWriter;

public class BaseDefaults {
	private static final Logger LOGGER = Logger.getLogger( BaseDefaults.class.getName());
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

	private static void check( PreferenceHash pref) {
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

	static void hashLoad( FileInputStream in, PreferenceHash prefs) throws IOException {
		IJson mJson = JsonReader.read( in);
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
			Object obj = prefs.getObject( key);
			if (obj instanceof String[]) {
				dst.printArr( key, prefs.getArray( key));
			}
			else if (obj instanceof Boolean) {
				dst.print( key, prefs.getBool( key));
			}
			else if (obj instanceof Integer) {
				dst.print( key, prefs.getInt( key));
			}
			else if (obj != null) {
				dst.print( key, obj);
			}
		}
		dst.endObject();
		dst.close();
	}
}
