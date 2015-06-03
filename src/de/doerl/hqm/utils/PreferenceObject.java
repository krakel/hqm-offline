package de.doerl.hqm.utils;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

class PreferenceObject {
	private static final String[] DUMMY_ARRAY = new String[0];
	private static final Logger LOGGER = Logger.getLogger( PreferenceObject.class.getName());
	private Object mValue;

	PreferenceObject() {
	}

	private static Boolean parseBoolean( Object value) {
		if (value != null) {
			if ("true".equalsIgnoreCase( value.toString())) {
				return Boolean.TRUE;
			}
			if ("false".equalsIgnoreCase( value.toString())) {
				return Boolean.FALSE;
			}
		}
		return null;
	}

	private static Integer parseInteger( Object value) {
		if (value != null) {
			try {
				return Integer.decode( value.toString());
			}
			catch (NumberFormatException e) {
			}
		}
		return null;
	}

	static String toString( Color val) {
		String s = Integer.toHexString( 0xFFFFFF & val.getRGB()).toUpperCase();
		StringBuffer sb = new StringBuffer( "0x");
		for (int i = s.length(); i < 6; ++i) {
			sb.append( '0');
		}
		sb.append( s);
		return sb.toString();
	}

	public void addArrayString( int pos, String value) {
		String[] arr = getArray();
		if (pos < 0) {
			pos = 0;
		}
		if (pos < arr.length) {
			addArrayValue( arr, pos, value);
		}
		else {
			addArrayValue( arr, arr.length, value);
		}
	}

	public void addArrayString( String value) {
		String[] arr = getArray();
		addArrayValue( arr, arr.length, value);
	}

	private void addArrayValue( String[] old, int pos, String value) {
		String[] arr = new String[old.length + 1];
		if (pos > 0) {
			System.arraycopy( old, 0, arr, 0, pos);
		}
		if (pos < old.length) {
			System.arraycopy( old, pos, arr, pos + 1, old.length - pos);
		}
		arr[pos] = value;
		setArray( arr);
	}

	public boolean containsArrayString( String value) {
		String[] arr = getArray();
		for (int i = 0; i < arr.length; ++i) {
			if (Utils.equals( value, arr[i])) {
				return true;
			}
		}
		return false;
	}

	public void decInteger() {
		setInt( getInt( 0) - 1);
	}

	public void deleteArrayString( int nr) {
		String[] arr = getArray();
		if (arr.length > 0) {
			deleteArrayValue( arr, nr);
		}
	}

	public void deleteArrayString( String value) {
		String[] arr = getArray();
		for (int i = 0; i < arr.length; ++i) {
			if (Utils.equals( value, arr[i])) {
				deleteArrayValue( arr, i);
				return;
			}
		}
	}

	private void deleteArrayValue( String[] old, int nr) {
		String[] arr = new String[old.length - 1];
		System.arraycopy( old, 0, arr, 0, nr);
		System.arraycopy( old, nr + 1, arr, nr, arr.length - nr);
		mValue = arr;
	}

	public String[] getArray() {
		try {
			return mValue != null ? (String[]) mValue : DUMMY_ARRAY;
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		try {
			return new String[] {
				(String) mValue
			};
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return DUMMY_ARRAY;
	}

	public String[] getArray( String[] def) {
		try {
			return mValue != null ? (String[]) mValue : def;
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
			return def;
		}
	}

	public boolean getBool( boolean def) {
		return getBoolean( def).booleanValue();
	}

	public Boolean getBoolean( Boolean def) {
		if (mValue instanceof Boolean) {
			return (Boolean) mValue;
		}
		Integer value = parseInteger( mValue);
		if (value != null) {
			return Boolean.valueOf( value.intValue() != 0);
		}
		Boolean bool = parseBoolean( mValue);
		return bool != null ? bool : def;
	}

	public Color getColor( Color def) {
		if (mValue instanceof Color) {
			return (Color) mValue;
		}
		Integer value = parseInteger( mValue);
		return value != null ? new Color( value.intValue()) : def;
	}

	public int getInt( int def) {
		return getInteger( def).intValue();
	}

	public Integer getInteger( Integer def) {
		if (mValue instanceof Integer) {
			return (Integer) mValue;
		}
		Integer value = parseInteger( mValue);
		return value != null ? value : def;
	}

	Object getObject() {
		return mValue;
	}

	public String getString( String def) {
		return mValue != null ? mValue.toString() : def;
	}

	public void incInteger() {
		setInt( getInt( 0) + 1);
	}

	public void setArray( String[] val) {
		mValue = val;
	}

	public void setArrayString( int nr, String value) {
		String[] arr = getArray();
		if (value != null) {
			if (nr >= 0 && nr < arr.length) {
				setArrayValue( arr, nr, value);
			}
			else if (nr == arr.length) {
				addArrayValue( arr, arr.length, value);
			}
		}
		else if (arr.length > 0) {
			deleteArrayValue( arr, nr);
		}
	}

	private void setArrayValue( String[] old, int nr, String value) {
		String[] result = new String[old.length];
		System.arraycopy( old, 0, result, 0, old.length);
		result[nr] = value;
		mValue = result;
	}

	public void setBool( boolean val) {
		mValue = val ? Boolean.TRUE : Boolean.FALSE;
	}

	public void setBoolean( Boolean val) {
		mValue = val;
	}

	public void setColor( Color val) {
		if (val != null) {
			mValue = val;
		}
		else {
			mValue = null;
		}
	}

	public void setInt( int val) {
		mValue = Integer.valueOf( val);
	}

	public void setInteger( Integer val) {
		mValue = val;
	}

	void setObject( Object obj) {
		mValue = obj;
	}

	public void setString( String val) {
		mValue = val;
	}

	@Override
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "value", mValue);
		return sb.toString();
	}
}
