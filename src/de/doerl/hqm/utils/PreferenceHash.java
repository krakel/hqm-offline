package de.doerl.hqm.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

class PreferenceHash extends HashMap<String, Object> {
	private static final String COUNT_SUFFIX = ".count";
	private static final String KEY_VALUE_SEPS = "=: \t\r\n\f";
	private static final Logger LOGGER = Logger.getLogger( PreferenceHash.class.getName());
	private static final long serialVersionUID = 5795682426715300638L;
	private static final String STRICT_VALUE_SEPS = "=:";
	private static final String WHITE_SPACE_CHARS = " \t\r\n\f";

	private static boolean continueLine( String line) {
		int slashCount = 0;
		int index = line.length() - 1;
		while (index >= 0 && line.charAt( index--) == '\\') {
			slashCount++;
		}
		return slashCount % 2 == 1;
	}

	static String loadConvert( String src) {
		int len = src.length();
		StringBuffer sb = new StringBuffer( len);
		for (int i = 0; i < len;) {
			char ch = src.charAt( i++);
			if (ch != '\\') {
				sb.append( ch);
				continue;
			}
			ch = src.charAt( i++);
			switch (ch) {
				case 't':
					ch = '\t';
					break;
				case 'r':
					ch = '\r';
					break;
				case 'n':
					ch = '\n';
					break;
				case 'f':
					ch = '\f';
					break;
				case 'u':
					// Read the xxxx
					int value = 0;
					for (int j = 0; j < 4; ++j) {
						ch = src.charAt( i++);
						switch (ch) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + ch - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + ch - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + ch - 'A';
								break;
							default:
								throw new IllegalArgumentException( "Malformed \\uxxxx encoding.");
						}
					}
					ch = (char) value;
					break;
			}
			sb.append( ch);
		}
		return sb.toString();
	}

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

	synchronized void checkLevel( String key) {
		if (!containsKey( key)) {
			setString( key, BaseDefaults.getDefaultLevel( key).getName());
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

	synchronized void load( InputStream is) throws IOException {
		BufferedReader in = new BufferedReader( new InputStreamReader( is, "8859_1"));
		while (true) {
			String line = in.readLine();
			if (line == null) {
				return;
			}
			int len = line.length();
			if (len > 0) {
				int start;
				for (start = 0; start < len; start++) {
					if (WHITE_SPACE_CHARS.indexOf( line.charAt( start)) == -1) {
						break;
					}
				}
				// Blank lines are ignored
				if (start == len) {
					continue;
				}
				// Continue lines that end in slashes if they are not comments
				char firstChar = line.charAt( start);
				if (firstChar != '#' && firstChar != '!') {
					while (continueLine( line)) {
						String nextLine = in.readLine();
						if (nextLine == null) {
							nextLine = "";
						}
						String loppedLine = line.substring( 0, len - 1);
						// Advance beyond whitespace on new line
						int startIndex;
						for (startIndex = 0; startIndex < nextLine.length(); startIndex++) {
							if (WHITE_SPACE_CHARS.indexOf( nextLine.charAt( startIndex)) == -1) {
								break;
							}
						}
						nextLine = nextLine.substring( startIndex, nextLine.length());
						line = new String( loppedLine + nextLine);
						len = line.length();
					}
					// Find separation between key and value
					int separatorIndex;
					for (separatorIndex = start; separatorIndex < len; separatorIndex++) {
						char currentChar = line.charAt( separatorIndex);
						if (currentChar == '\\') {
							separatorIndex++;
						}
						else if (KEY_VALUE_SEPS.indexOf( currentChar) != -1) {
							break;
						}
					}
					// Skip over whitespace after key if any
					int valueIndex;
					for (valueIndex = separatorIndex; valueIndex < len; valueIndex++) {
						if (WHITE_SPACE_CHARS.indexOf( line.charAt( valueIndex)) == -1) {
							break;
						}
					}
					// Skip over one non whitespace key value separators if any
					if (valueIndex < len) {
						if (STRICT_VALUE_SEPS.indexOf( line.charAt( valueIndex)) != -1) {
							valueIndex++;
						}
					}
					// Skip over white space after other separators if any
					while (valueIndex < len) {
						if (WHITE_SPACE_CHARS.indexOf( line.charAt( valueIndex)) == -1) {
							break;
						}
						valueIndex++;
					}
					String key = line.substring( start, separatorIndex);
					String value = separatorIndex < len ? line.substring( valueIndex, len) : "";
					key = loadConvert( key);
					if (!key.endsWith( COUNT_SUFFIX)) {
						value = loadConvert( value);
						try {
							int pos = key.lastIndexOf( '.');
							Integer.parseInt( key.substring( pos + 1));
							key = key.substring( 0, pos);
							addArrayString( key, value);
						}
						catch (Exception ex) {
							setString( key, value);
						}
//						setSave( key, true);
					}
				}
			}
		}
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

	void setSave( String key, boolean value) {
		getValue( key).setSave( value);
	}

	boolean setString( String key, String val) {
		PreferenceObject pref = getValue( key);
		if (Utils.different( val, pref.getString( null))) {
			pref.setString( val);
			return true;
		}
		return false;
	}

	void store( OutputStream out, String header) throws IOException {
		BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( out, "8859_1"));
		if (header != null) {
			bw.write( "# " + header);
			bw.newLine();
		}
		bw.write( "# " + new Date().toString());
		bw.newLine();
		synchronized (this) {
			for (String key : keySet()) {
				if (BaseDefaults.isKeyForSave( key)) {
					getValue( key).write( bw, key);
				}
			}
		}
		bw.write( '\n');
		bw.flush();
	}
}
