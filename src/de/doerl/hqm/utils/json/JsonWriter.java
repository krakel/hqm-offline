package de.doerl.hqm.utils.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import de.doerl.hqm.utils.Security;
import de.doerl.hqm.utils.Tabber;

public class JsonWriter {
	private static final String NL = Security.getProperty( "line.separator", "\n");
	private Tabber mTabs = new Tabber();
	private PrintWriter mOut;

	public JsonWriter( OutputStream out) throws IOException {
		mOut = new PrintWriter( new OutputStreamWriter( out, "UTF-8"));
	}

	public JsonWriter( Writer out) {
		mOut = new PrintWriter( out);
	}

	public void beginArray() {
		checkNL();
		mOut.print( "[");
		mTabs.inc();
	}

	public void beginArray( String key) {
		checkNL();
		writeKey( key);
		mOut.print( "[");
		mTabs.inc();
	}

	public void beginObject() {
		checkNL();
		mOut.print( "{");
		mTabs.inc();
	}

	public void beginObject( String key) {
		checkNL();
		writeKey( key);
		mOut.print( "{");
		mTabs.inc();
	}

	private void checkNL() {
		if (mTabs.isNL()) {
			if (mTabs.isComma()) {
				mOut.write( ',');
			}
			mOut.write( NL);
			mOut.write( mTabs.toString());
		}
	}

	public void endArray() {
		mTabs.dec();
		checkNL();
		mOut.print( "]");
		writeNL();
	}

	public void endObject() {
		mTabs.dec();
		checkNL();
		mOut.print( "}");
		writeNL();
	}

	public void flush() {
		if (mTabs.isNL()) {
			mOut.write( NL);
		}
		mOut.flush();
	}

	public void print( String key, boolean value) {
		checkNL();
		writeKey( key);
		printValue( value);
	}

	public void print( String key, byte value) {
		checkNL();
		writeKey( key);
		printValue( value);
	}

	public void print( String key, double value) {
		checkNL();
		writeKey( key);
		printValue( value);
	}

	public void print( String key, float value) {
		checkNL();
		writeKey( key);
		printValue( value);
	}

	public void print( String key, int value) {
		checkNL();
		writeKey( key);
		printValue( value);
	}

	public void print( String key, long value) {
		checkNL();
		writeKey( key);
		printValue( value);
	}

	public void print( String key, Object obj) {
		checkNL();
		writeKey( key);
		writeObj( obj);
		writeNL();
	}

	public void print( String key, short value) {
		checkNL();
		writeKey( key);
		printValue( value);
	}

	public void printArr( String key, boolean[] arr) {
		beginArray( key);
		for (int i = 0; i < arr.length; ++i) {
			printValue( arr[i]);
		}
		endArray();
	}

	public void printArr( String key, byte[] arr) {
		beginArray( key);
		for (int i = 0; i < arr.length; ++i) {
			printValue( arr[i]);
		}
		endArray();
	}

	public void printArr( String key, double[] arr) {
		beginArray( key);
		for (int i = 0; i < arr.length; ++i) {
			printValue( arr[i]);
		}
		endArray();
	}

	public void printArr( String key, float[] arr) {
		beginArray( key);
		for (int i = 0; i < arr.length; ++i) {
			printValue( arr[i]);
		}
		endArray();
	}

	public void printArr( String key, int[] arr) {
		beginArray( key);
		for (int i = 0; i < arr.length; ++i) {
			printValue( arr[i]);
		}
		endArray();
	}

	public void printArr( String key, long[] arr) {
		beginArray( key);
		for (int i = 0; i < arr.length; ++i) {
			printValue( arr[i]);
		}
		endArray();
	}

	public void printArr( String key, Object[] arr) {
		beginArray( key);
		for (int i = 0; i < arr.length; ++i) {
			printValue( arr[i]);
		}
		endArray();
	}

	public void printArr( String key, short[] arr) {
		beginArray( key);
		for (int i = 0; i < arr.length; ++i) {
			printValue( arr[i]);
		}
		endArray();
	}

	public void printIf( String key, Object obj) {
		if (obj != null) {
			checkNL();
			writeKey( key);
			writeObj( obj);
			writeNL();
		}
	}

	public void printValue( boolean value) {
		checkNL();
		mOut.print( value);
		writeNL();
	}

	public void printValue( byte value) {
		checkNL();
		mOut.print( value);
		writeNL();
	}

	public void printValue( double value) {
		checkNL();
		mOut.print( value);
		writeNL();
	}

	public void printValue( float value) {
		checkNL();
		mOut.print( value);
		writeNL();
	}

	public void printValue( int value) {
		checkNL();
		mOut.print( value);
		writeNL();
	}

	public void printValue( long value) {
		checkNL();
		mOut.print( value);
		writeNL();
	}

	public void printValue( Object obj) {
		checkNL();
		writeObj( obj);
		writeNL();
	}

	public void printValue( short value) {
		checkNL();
		mOut.print( value);
		writeNL();
	}

	public void writeKey( String key) {
		mOut.print( '"');
		mOut.print( key);
		mOut.print( '"');
		mOut.print( " : ");
	}

	private void writeNL() {
		mTabs.nl();
	}

	private void writeObj( Object obj) {
		if (obj != null) {
			mOut.print( '"');
			mOut.print( Escape.escape( obj.toString()));
			mOut.print( '"');
		}
		else {
			mOut.println( "null");
		}
	}
}
