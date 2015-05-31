package de.doerl.hqm.medium.json;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import de.doerl.hqm.utils.Helper;
import de.doerl.hqm.utils.Nbt;
import de.doerl.hqm.utils.Security;
import de.doerl.hqm.utils.Tabber;

class JsonWriter extends PrintWriter {
	private static final Charset UTF_8 = Charset.forName( "UTF-8");
	private static final String NL = Security.getProperty( "line.separator", "\n");
	private Tabber mTabs = new Tabber();

	public JsonWriter( OutputStream out) {
		super( out);
	}

	public JsonWriter( Writer out) {
		super( out);
	}

	private static String escape( String s) {
		StringBuffer sb = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; ++i) {
			char c = s.charAt( i);
			switch (c) {
				case '\b':
					sb.append( "\\b");
					break;
				case '\f':
					sb.append( "\\f");
					break;
				case '\n':
					sb.append( "\\n");
					break;
				case '\r':
					sb.append( "\\r");
					break;
				case '\t':
					sb.append( "\\t");
					break;
				case '"':
					sb.append( "\\\"");
					break;
				case '\\':
					sb.append( "\\\\");
					break;
				case '/':
					sb.append( "\\/");
					break;
				default:
					if (c >= '\u0000' && c <= '\u001F' || c >= '\u007F' && c <= '\u009F' || c >= '\u2000' && c <= '\u20FF') {
						String hex = Integer.toHexString( c);
						sb.append( "\\u");
						for (int j = hex.length(); j < 4; ++j) {
							sb.append( '0');
						}
						sb.append( hex.toUpperCase());
					}
					else {
						sb.append( c);
					}
			}
		}
		return sb.toString();
	}

	public void beginArray( String key) {
		printKey( key);
		print( "[");
		mTabs.inc();
	}

	public void beginObject() {
		print( "{");
		mTabs.inc();
	}

	private void checkNL() {
		if (mTabs.isNL()) {
			if (mTabs.isComma()) {
				super.write( ',');
			}
			super.write( NL);
			super.write( mTabs.toString());
		}
	}

	@Override
	public void close() {
		if (mTabs.isNL()) {
			super.write( NL);
		}
		super.close();
	}

	public void endArray() {
		mTabs.dec();
		print( "]");
		println();
	}

	public void endObject() {
		mTabs.dec();
		print( "}");
		println();
	}

	public Tabber getTabber() {
		return mTabs;
	}

	public void print( String key, boolean value) {
		printKey( key);
		print( String.valueOf( value));
		println();
	}

	public void print( String key, byte value) {
		printKey( key);
		print( String.valueOf( value));
		println();
	}

	public void print( String key, byte[] arr) {
		printKey( key);
		print( Helper.toHex( arr));
		println();
	}

	public void print( String key, double value) {
		printKey( key);
		print( String.valueOf( value));
		println();
	}

	public void print( String key, float value) {
		printKey( key);
		print( String.valueOf( value));
		println();
	}

	public void print( String key, int value) {
		printKey( key);
		print( String.valueOf( value));
		println();
	}

	public void print( String key, Object obj) {
		printKey( key);
		if (obj != null) {
			printObj( obj);
		}
		else {
			println( "null");
		}
	}

	public void print( String key, short value) {
		printKey( key);
		print( String.valueOf( value));
		println();
	}

	private void printArr( byte[] arr) {
		for (byte b : arr) {
			super.write( b);
		}
	}

	public void printIf( String key, Object obj) {
		if (obj != null) {
			printKey( key);
			printObj( obj);
		}
	}

	public void printKey( String key) {
		print( '"');
		printArr( key.getBytes( UTF_8));
		print( '"');
		print( " : ");
	}

	@Override
	public void println() {
		mTabs.setNL();
	}

	public void printNBT( Nbt nbt) {
		print( '"');
		print( nbt.toString());
		print( '"');
		println();
	}

	public void printNBT( String key, Nbt nbt) {
		if (nbt != null) {
			printKey( key);
			printNBT( nbt);
		}
	}

	public void printObj( Object obj) {
		print( '"');
		print( escape( obj.toString()));
		print( '"');
		println();
	}

	@Override
	public void write( int c) {
		checkNL();
		super.write( c);
	}

	@Override
	public void write( String s) {
		checkNL();
		super.write( s);
	}
}
