package de.doerl.hqm.utils.nbt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import de.doerl.hqm.utils.Utils;

public class ParserAtBits {
	private static final Logger LOGGER = Logger.getLogger( ParserAtBits.class.getName());
	private byte[] mSrc;
	private int mPos;

	private ParserAtBits( byte[] src) {
		mSrc = src;
	}

	private static void decompress( InputStream in, OutputStream out) {
		GZIPInputStream is = null;
		try {
			is = new GZIPInputStream( in);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read( buffer)) != -1) {
				out.write( buffer, 0, len);
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( is);
		}
	}

	static FCompound parseAsCompound( byte[] arr) {
		ParserAtBits rdr = new ParserAtBits( arr);
		return rdr.doMain();
	}

	private static byte[] read( InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		decompress( in, out);
		return out.toByteArray();
	}

	public static FCompound readAsCompound( InputStream in) {
		byte[] src = read( in);
		return parseAsCompound( src);
	}

	private FByteArray doArrByte( String name) {
		FByteArray arr = new FByteArray( name);
		int count = doInt();
		for (int i = 0; i < count; ++i) {
			arr.add( doByte());
		}
		return arr;
	}

	private FIntArray doArrInt( String name) {
		FIntArray arr = new FIntArray( name);
		int count = doInt();
		for (int i = 0; i < count; ++i) {
			arr.add( doInt());
		}
		return arr;
	}

	private byte doByte() {
		byte value = ANbt.getByte( mSrc, mPos);
		mPos++;
		return value;
	}

	private void doCompound( FCompound main) {
		while (mPos < mSrc.length) {
			int tag = doByte();
			if (tag == 0) {
				break;
			}
			main.add( doValue( tag, doString()));
		}
	}

	private double doDouble() {
		double value = ANbt.getDouble( mSrc, mPos);
		mPos += 8;
		return value;
	}

	private float doFloat() {
		float value = ANbt.getFloat( mSrc, mPos);
		mPos += 4;
		return value;
	}

	private int doInt() {
		int value = ANbt.getInt( mSrc, mPos);
		mPos += 4;
		return value;
	}

	private AList doList( String name) {
		FList list = new FList( name);
		int tag = doByte();
		int count = doInt();
		for (int i = 0; i < count; ++i) {
			list.add( doValue( tag, ""));
		}
		return list;
	}

	private long doLong() {
		long value = ANbt.getLong( mSrc, mPos);
		mPos += 8;
		return value;
	}

	private FCompound doMain() {
		FCompound main = new FCompound( "");
		int tag = doByte();
		if (tag == 10) {
			doString();
			doCompound( main);
		}
		return main;
	}

	private int doShort() {
		int value = ANbt.getShort( mSrc, mPos);
		mPos += 2;
		return value;
	}

	private String doString() {
		int len = doShort();
		String str = ANbt.getString( mSrc, mPos, len);
		mPos += len;
		return str.replace( "'", "\\'");
	}

	private ANbt doValue( int tag, String name) {
		switch (tag) {
			case 1: // Byte
				return FLong.createByte( name, doByte());
			case 2: // Short
				return FLong.createShort( name, doShort());
			case 3: // Int
				return FLong.createInt( name, doInt());
			case 4: // Long
				return FLong.createLong( name, doLong());
			case 5: // Float
				return FDouble.createFloat( name, doFloat());
			case 6: // Double
				return FDouble.createDouble( name, doDouble());
			case 7: // Byte-Array
				return doArrByte( name);
			case 8: // String
				return FString.create( name, doString());
			case 9: // List
				return doList( name);
			case 10: // Compound
				FCompound main = new FCompound( name);
				doCompound( main);
				return main;
			case 11: // Int-Array
				return doArrInt( name);
			default:
				return null;
		}
	}
}
