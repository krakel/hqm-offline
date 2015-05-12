package de.doerl.hqm.utils;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings( "nls")
class PreferenceObject {
	private static final String COUNT_SUFFIX = ".count";
	private static final String[] DUMMY_ARRAY = new String[0];
	private static final char[] HEXDIGIT = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	};
	private static final Logger LOGGER = Logger.getLogger( PreferenceObject.class.getName());
	private static final String SPECIAL_SAVE_CHARS = "=: \t\r\n\f#!";
	private Object mValue;
	private boolean mSave;

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

	static String saveConvert( String src, boolean esc) {
		int len = src.length();
		StringBuffer sb = new StringBuffer( len * 2);
		for (int i = 0; i < len; ++i) {
			char ch = src.charAt( i);
			switch (ch) {
				case ' ':
					if (i == 0 || esc) {
						sb.append( '\\');
					}
					sb.append( ' ');
					break;
				case '\\':
					sb.append( '\\');
					sb.append( '\\');
					break;
				case '\t':
					sb.append( '\\');
					sb.append( 't');
					break;
				case '\n':
					sb.append( '\\');
					sb.append( 'n');
					break;
				case '\r':
					sb.append( '\\');
					sb.append( 'r');
					break;
				case '\f':
					sb.append( '\\');
					sb.append( 'f');
					break;
				default:
					if (ch < 0x0020 || ch > 0x007e) {
						sb.append( '\\');
						sb.append( 'u');
						sb.append( HEXDIGIT[ch >> 12 & 0xF]);
						sb.append( HEXDIGIT[ch >> 8 & 0xF]);
						sb.append( HEXDIGIT[ch >> 4 & 0xF]);
						sb.append( HEXDIGIT[ch & 0xF]);
					}
					else {
						if (SPECIAL_SAVE_CHARS.indexOf( ch) != -1) {
							sb.append( '\\');
						}
						sb.append( ch);
					}
			}
		}
		return sb.toString();
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
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
		}
		try {
			return new String[] {
				(String) mValue
			};
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
		}
		return DUMMY_ARRAY;
	}

	public String[] getArray( String[] def) {
		try {
			return mValue != null ? (String[]) mValue : def;
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
			return def;
		}
	}

	public boolean getBool( boolean def) {
		Integer value = parseInteger( mValue);
		if (value != null) {
			return value.intValue() != 0;
		}
		Boolean bool = parseBoolean( mValue);
		return bool != null ? bool.booleanValue() : def;
	}

	public Boolean getBoolean( Boolean def) {
		Integer value = parseInteger( mValue);
		if (value != null) {
			return Boolean.valueOf( value.intValue() != 0);
		}
		Boolean bool = parseBoolean( mValue);
		return bool != null ? bool : def;
	}

	public Color getColor( Color def) {
		Integer value = parseInteger( mValue);
		return value != null ? new Color( value.intValue()) : def;
	}

	public int getInt( int def) {
		Integer value = parseInteger( mValue);
		return value != null ? value.intValue() : def;
	}

	public Integer getInteger( Integer def) {
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

	public boolean isSave() {
		return mSave;
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
		mValue = val ? "true" : "false";
	}

	public void setBoolean( Boolean val) {
		mValue = val.toString();
	}

	public void setColor( Color val) {
		if (val != null) {
			mValue = toString( val);
		}
		else {
			mValue = null;
		}
	}

	public void setInt( int val) {
		mValue = Integer.toString( val);
	}

	public void setInteger( Integer val) {
		mValue = String.valueOf( val);
	}

	void setObject( Object obj) {
		mValue = obj;
	}

	void setSave( boolean value) {
		mSave = value;
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

	void write( BufferedWriter bw, Object key) throws IOException {
		if (mValue == null) {
			return;
		}
		if (mValue instanceof String[]) {
			String[] old = getArray();
			int size = 0;
			for (int i = 0; i < old.length; ++i) {
				String value = old[i];
				bw.write( saveConvert( key.toString() + '.' + size, true) + " = " + saveConvert( value, false));
				bw.newLine();
				++size;
			}
			if (size > 0) {
				bw.write( saveConvert( key.toString() + COUNT_SUFFIX, true) + " = " + size);
				bw.newLine();
			}
		}
		else {
			bw.write( saveConvert( key.toString(), true) + " = " + saveConvert( mValue.toString(), false));
			bw.newLine();
		}
	}
}
