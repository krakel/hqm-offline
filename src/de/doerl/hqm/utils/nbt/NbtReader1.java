/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
package de.doerl.hqm.utils.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import de.doerl.hqm.utils.Utils;

public class NbtReader1 {
	private static final Logger LOGGER = Logger.getLogger( NbtReader1.class.getName());
	private byte[] mSrc;
	private int mPos;

	private NbtReader1( byte[] src) {
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
		NbtReader1 rdr = new NbtReader1( arr);
		return rdr.doCompound( "");
	}

	private static byte[] read( InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		decompress( in, out);
		return out.toByteArray();
	}

	public static FCompound readAsCompound( byte[] arr) {
		byte[] src = read( new ByteArrayInputStream( arr));
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

	private FCompound doCompound( String name) {
		FCompound main = new FCompound( name);
		while (mPos < mSrc.length) {
			int tag = doByte();
			if (tag == 0) {
				break;
			}
			doValue( tag, doString(), main);
		}
		return main;
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
		int tag = doByte();
		int count = doInt();
		FList list = new FList( name, tag);
		for (int i = 0; i < count; ++i) {
			doValue( tag, "", list);
		}
		return list;
	}

	private long doLong() {
		long value = ANbt.getLong( mSrc, mPos);
		mPos += 8;
		return value;
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

	private void doValue( int tag, String name, AList list) {
		switch (tag) {
			case 0: // End
				break;
			case 1: // Byte
				list.add( new FLong( name, doByte(), tag));
				break;
			case 2: // Short
				list.add( new FLong( name, doShort(), tag));
				break;
			case 3: // Int
				list.add( new FLong( name, doInt(), tag));
				break;
			case 4: // Long
				list.add( new FLong( name, doLong(), tag));
				break;
			case 5: // Float
				list.add( new FDouble( name, doFloat(), tag));
				break;
			case 6: // Double
				list.add( new FDouble( name, doDouble(), tag));
				break;
			case 7: // Byte-Array
				list.add( doArrByte( name));
				break;
			case 8: // String
				list.add( new FString( name, doString()));
				break;
			case 9: // List
				list.add( doList( name));
				break;
			case 10: // Compound
				list.add( doCompound( name));
				break;
			case 11: // Int-Array
				list.add( doArrInt( name));
				break;
		}
	}
}
