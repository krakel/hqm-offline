package de.doerl.hqm.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

class PreferenceHash extends HashMap<String, Object> {
	private static final Logger LOGGER = Logger.getLogger( PreferenceHash.class.getName());
	private static final long serialVersionUID = 5795682426715300638L;

	void addArrayString( String key, int pos, String value) {
		getValue( key).addArrayString( pos, value);
	}

	void addArrayString( String key, String value) {
		getValue( key).addArrayString( value);
	}

	synchronized void checkArray( String key) {
		if (!containsKey( key)) {
			setArray( key, BaseDefaults.getDefaultArray( key));
		}
	}

	synchronized void checkBool( String key) {
		boolean def = BaseDefaults.getDefaultBoolean( key);
		if (containsKey( key)) {
			def = getBool( key, def); // correct 1,0 to true, false
		}
		setBool( key, def);
	}

	synchronized void checkColor( String key) {
		if (!containsKey( key)) {
			Color def = BaseDefaults.getDefaultColor( key);
			setColor( key, def);
		}
	}

	synchronized void checkInteger( String key) {
		if (!containsKey( key)) {
			setInt( key, BaseDefaults.getDefaultInteger( key));
		}
	}

	synchronized void checkMaximum( String key, int max, String msg) {
		int val = getInt( key);
		if (val > max) {
			setInt( key, max);
			Utils.log( LOGGER, Level.INFO, msg);
		}
	}

	synchronized void checkMinimum( String key, int min, String msg) {
		int val = getInt( key);
		if (val < min) {
			setInt( key, min);
			Utils.log( LOGGER, Level.INFO, msg);
		}
	}

	synchronized void checkSelect( String key) {
		String[] def = BaseDefaults.getDefaultArray( key);
		String val = getString( null, key);
		if (val != null) {
			for (int i = 0; i < def.length; ++i) {
				if (Utils.equals( val, def[i])) {
					return;
				}
			}
		}
		setString( key, def.length > 0 ? def[0] : null);
	}

	synchronized void checkString( String key) {
		if (!containsKey( key)) {
			setString( key, BaseDefaults.getDefaultString( key));
		}
	}

	boolean containsArrayString( String key, String value) {
		return getValue( key).containsArrayString( value);
	}

	synchronized void correctKey( String key, String prefix) {
		for (Iterator<String> e = keySet().iterator(); e.hasNext();) {
			try {
				String akt = e.next();
				if (akt.startsWith( key)) {
					renameKey( prefix + akt, akt);
				}
			}
			catch (ClassCastException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	void decInteger( String key) {
		getValue( key).decInteger();
	}

	void deleteArrayString( String key, int nr) {
		getValue( key).deleteArrayString( nr);
	}

	void deleteArrayString( String key, String value) {
		getValue( key).deleteArrayString( value);
	}

	String[] getArray( String key) {
		return getValue( key).getArray();
	}

	String[] getArray( String key, String[] def) {
		return getValue( key).getArray( def);
	}

	int getArrayCount( String key) {
		return getArray( key).length;
	}

	String getArrayString( String key, int nr) {
		String[] arr = getArray( key);
		if (nr >= 0 && nr < arr.length) {
			return arr[nr];
		}
		return null;
	}

	boolean getBool( String key) {
		return getBool( key, false);
	}

	boolean getBool( String key, boolean def) {
		return getValue( key).getBool( def);
	}

	Boolean getBoolean( String key) {
		return getBoolean( key, Boolean.FALSE);
	}

	Boolean getBoolean( String key, Boolean def) {
		return getValue( key).getBoolean( def);
	}

	Color getColor( String key) {
		return getColor( key, null);
	}

	Color getColor( String key, Color def) {
		return getValue( key).getColor( def);
	}

	int getInt( String key) {
		return getInt( key, 0);
	}

	int getInt( String key, int def) {
		return getValue( key).getInt( def);
	}

	Integer getInteger( String key) {
		return getInteger( key, null);
	}

	Integer getInteger( String key, Integer def) {
		return getValue( key).getInteger( def);
	}

	Object getObject( String key) {
		return getValue( key).getObject();
	}

	String getString( String key) {
		return getString( key, null);
	}

	String getString( String key, String def) {
		return getValue( key).getString( def);
	}

	synchronized PreferenceObject getValue( String key) {
		PreferenceObject pref = (PreferenceObject) get( key);
		if (pref == null) {
			pref = new PreferenceObject();
			put( key, pref);
		}
		return pref;
	}

	void incInteger( String key) {
		getValue( key).incInteger();
	}

	synchronized void renameKey( String key, String oldKey) {
		Object old = remove( oldKey);
		if (old != null) {
			put( key, old);
		}
	}

	void setArray( String key, String[] val) {
		getValue( key).setArray( val);
	}

	void setArraySize( String key, int size) {
		getValue( key).setArraySize( size);
	}

	void setArrayString( String key, int nr, String value) {
		getValue( key).setArrayString( nr, value);
	}

	boolean setBool( String key, boolean val) {
		PreferenceObject pref = getValue( key);
		if (val != pref.getBool( false)) {
			pref.setBool( val);
			return true;
		}
		return false;
	}

	boolean setBoolean( String key, Boolean val) {
		PreferenceObject pref = getValue( key);
		if (Utils.different( val, pref.getBoolean( Boolean.FALSE))) {
			pref.setBoolean( val);
			return true;
		}
		return false;
	}

	boolean setColor( String key, Color val) {
		PreferenceObject pref = getValue( key);
		if (Utils.different( val, pref.getColor( null))) {
			pref.setColor( val);
			return true;
		}
		return false;
	}

	boolean setInt( String key, int val) {
		PreferenceObject pref = getValue( key);
		if (val != pref.getInt( Integer.MIN_VALUE)) {
			pref.setInt( val);
			return true;
		}
		return false;
	}

	boolean setInteger( String key, Integer val) {
		PreferenceObject pref = getValue( key);
		if (Utils.different( val, pref.getInteger( null))) {
			pref.setInteger( val);
			return true;
		}
		return false;
	}

	void setObject( String key, Object val) {
		getValue( key).setObject( val);
	}

	boolean setString( String key, String val) {
		PreferenceObject pref = getValue( key);
		if (Utils.different( val, pref.getString( null))) {
			pref.setString( val);
			return true;
		}
		return false;
	}
}
