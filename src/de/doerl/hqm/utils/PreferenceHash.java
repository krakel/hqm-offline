package de.doerl.hqm.utils;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class PreferenceHash extends HashMap<String, Object> {
	private static final long serialVersionUID = 5795682426715300638L;
	private static final Logger LOGGER = Logger.getLogger( PreferenceHash.class.getName());
	private static final String[] DUMMY_ARRAY = new String[0];

	static String[] toArray( Object obj) {
		try {
			return obj != null ? (String[]) obj : DUMMY_ARRAY;
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		try {
			return new String[] {
				(String) obj
			};
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return DUMMY_ARRAY;
	}

	private static String[] toArray( Object obj, String[] def) {
		try {
			return obj != null ? (String[]) obj : def;
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return def;
	}

	static boolean toBool( Object obj) {
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		}
		Boolean bool = toBoolean( obj);
		if (bool != null) {
			return bool;
		}
		Integer number = toInteger( obj);
		if (number != null) {
			return number.intValue() != 0;
		}
		return false;
	}

	private static Boolean toBoolean( Object obj) {
		if (obj != null) {
			if ("true".equalsIgnoreCase( obj.toString())) {
				return Boolean.TRUE;
			}
			if ("false".equalsIgnoreCase( obj.toString())) {
				return Boolean.FALSE;
			}
		}
		return null;
	}

	static Boolean toBoolean( Object obj, Boolean def) {
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		}
		Boolean bool = toBoolean( obj);
		if (bool != null) {
			return bool;
		}
		Integer number = toInteger( obj);
		if (number != null) {
			return Boolean.valueOf( number.intValue() != 0);
		}
		return def;
	}

	static int toInt( Object obj) {
		Integer number = toInteger( obj);
		return number != null ? number.intValue() : 0;
	}

	private static Integer toInteger( Object value) {
		if (value != null) {
			try {
				return Integer.decode( value.toString());
			}
			catch (NumberFormatException ex) {
			}
		}
		return null;
	}

	void addArrayString( String key, int pos, String value) {
		if (pos < 0) {
			pos = 0;
		}
		String[] old = getArray( key);
		if (pos > old.length) {
			pos = old.length;
		}
		addArrayValue( key, old, pos, value);
	}

	void addArrayString( String key, String value) {
		String[] old = getArray( key);
		addArrayValue( key, old, old.length, value);
	}

	private void addArrayValue( String key, String[] old, int pos, String value) {
		String[] arr = new String[old.length + 1];
		if (pos > 0) {
			System.arraycopy( old, 0, arr, 0, pos);
		}
		if (pos < old.length) {
			System.arraycopy( old, pos, arr, pos + 1, old.length - pos);
		}
		arr[pos] = value;
		put( key, arr);
	}

	boolean containsArrayString( String key, String value) {
		String[] arr = getArray( key);
		for (int i = 0; i < arr.length; ++i) {
			if (Utils.equals( value, arr[i])) {
				return true;
			}
		}
		return false;
	}

	void decInteger( String key) {
		put( key, toInt( get( key)) - 1);
	}

	void deleteArrayString( String key, int nr) {
		String[] arr = getArray( key);
		if (arr.length > 0) {
			deleteArrayValue( key, arr, nr);
		}
	}

	void deleteArrayString( String key, String value) {
		String[] arr = getArray( key);
		for (int i = 0; i < arr.length; ++i) {
			if (Utils.equals( value, arr[i])) {
				deleteArrayValue( key, arr, i);
				return;
			}
		}
	}

	private void deleteArrayValue( String key, String[] old, int nr) {
		String[] arr = new String[old.length - 1];
		System.arraycopy( old, 0, arr, 0, nr);
		System.arraycopy( old, nr + 1, arr, nr, arr.length - nr);
		put( key, arr);
	}

	String[] getArray( String key) {
		return toArray( get( key));
	}

	String[] getArray( String key, String[] def) {
		Object old = get( key);
		return toArray( old, def);
	}

	int getArrayLength( String key) {
		return getArray( key).length;
	}

	String getArrayString( String key, int idx) {
		String[] arr = getArray( key);
		if (idx >= 0 && idx < arr.length) {
			return arr[idx];
		}
		return null;
	}

	boolean getBool( String key) {
		return getBool( key, false);
	}

	boolean getBool( String key, boolean def) {
		return toBoolean( get( key), def).booleanValue();
	}

	Boolean getBoolean( String key) {
		return getBoolean( key, Boolean.FALSE);
	}

	Boolean getBoolean( String key, Boolean def) {
		return toBoolean( get( key), def);
	}

	int getInt( String key) {
		return getInt( key, 0);
	}

	int getInt( String key, int def) {
		return toInt( get( key));
	}

	Integer getInteger( String key) {
		return getInteger( key, null);
	}

	Integer getInteger( String key, Integer def) {
		Object old = get( key);
		if (old instanceof Integer) {
			return (Integer) old;
		}
		Integer number = toInteger( old);
		return number != null ? number : def;
	}

	String getString( String key) {
		return getString( key, null);
	}

	String getString( String key, String def) {
		Object old = get( key);
		return old != null ? old.toString() : def;
	}

	void incInteger( String key) {
		put( key, toInt( get( key)) + 1);
	}

	void setArray( String key, String[] val) {
		put( key, val);
	}

	void setArraySize( String key, int size) {
		if (size < 0) {
			size = 0;
		}
		String[] old = getArray( key);
		String[] arr = new String[size];
		System.arraycopy( old, 0, arr, 0, Math.min( size, old.length));
		put( key, arr);
	}

	void setArrayString( String key, int nr, String value) {
		String[] old = getArray( key);
		if (value != null) {
			if (nr >= 0 && nr < old.length) {
				setArrayValue( key, old, nr, value);
			}
			else if (nr == old.length) {
				addArrayValue( key, old, old.length, value);
			}
		}
		else if (old.length > 0) {
			deleteArrayValue( key, old, nr);
		}
	}

	private void setArrayValue( String key, String[] old, int nr, String value) {
		String[] arr = new String[old.length];
		System.arraycopy( old, 0, arr, 0, old.length);
		arr[nr] = value;
		put( key, arr);
	}

	boolean setBool( String key, boolean val) {
		boolean old = toBoolean( get( key), false).booleanValue();
		if (val != old) {
			put( key, val ? Boolean.TRUE : Boolean.FALSE);
			return true;
		}
		return false;
	}

	boolean setBoolean( String key, Boolean val) {
		Boolean old = toBoolean( get( key), Boolean.FALSE);
		if (Utils.different( val, old)) {
			put( key, val);
			return true;
		}
		return false;
	}

	boolean setInt( String key, int val) {
		int old = toInt( get( key));
		if (val != old) {
			put( key, val);
			return true;
		}
		return false;
	}

	boolean setInteger( String key, Integer val) {
		Integer old = toInteger( get( key));
		if (Utils.different( val, old)) {
			put( key, val);
			return true;
		}
		return false;
	}

	boolean setString( String key, String val) {
		Object old = get( key);
		if (Utils.different( val, old)) {
			put( key, val);
			return true;
		}
		return false;
	}
}
