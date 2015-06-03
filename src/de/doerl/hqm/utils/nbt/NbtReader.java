package de.doerl.hqm.utils.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import de.doerl.hqm.utils.Helper;
import de.doerl.hqm.utils.Utils;

public class NbtReader {
	private static final Logger LOGGER = Logger.getLogger( NbtReader.class.getName());
	StringBuilder mSB = new StringBuilder();
	byte[] mSrc;

	private NbtReader( byte[] src) {
		mSrc = src;
	}

	static byte[] decompress( byte[] bytes) {
		ByteArrayOutputStream dst = new ByteArrayOutputStream();
		GZIPInputStream is = null;
		try {
			is = new GZIPInputStream( new ByteArrayInputStream( bytes));
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read( buffer)) != -1) {
				dst.write( buffer, 0, len);
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( is);
		}
		return dst.toByteArray();
	}

	public static String read( byte[] bytes) {
		byte[] src = decompress( bytes);
		return read0( src);
	}

	static String read0( byte[] src) {
		NbtReader rdr = new NbtReader( src);
		rdr.doAll( 0);
		return rdr.toString();
	}

	private int doAll( int pos) {
		boolean comma = false;
		while (pos >= 0 && pos < mSrc.length) {
			int tag = Helper.getByte( mSrc, pos);
			pos += 1;
			if (tag == 0) {
				break;
			}
			if (comma) {
				mSB.append( ", ");
			}
			pos = doKey( tag, pos);
			pos = doValue( tag, pos);
			comma = true;
		}
		return pos;
	}

	private int doArrByte( int pos) {
		int count = Helper.getInt( mSrc, pos);
		pos += 4;
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				mSB.append( ", ");
			}
			mSB.append( Helper.getByte( mSrc, pos));
			pos += 1;
		}
		return pos;
	}

	private int doArrInt( int pos) {
		int count = Helper.getInt( mSrc, pos);
		pos += 4;
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				mSB.append( ", ");
			}
			mSB.append( Helper.getInt( mSrc, pos));
			pos += 4;
		}
		return pos;
	}

	private int doArrList( int pos, int tag) {
		int count = Helper.getInt( mSrc, pos);
		pos += 4;
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				mSB.append( ", ");
			}
			pos = doValue( tag, pos);
		}
		return pos;
	}

	private int doKey( int tag, int pos) {
		switch (tag) {
			case 1: // Byte
			case 2: // Short
			case 3: // Int
			case 4: // Long
			case 5: // Float
			case 6: // Double
			case 7: // Byte-Array
			case 8: // String
			case 9: // List
			case 10: // Compound
			case 11: // Int-Array
				pos = doString( pos);
				mSB.append( "=");
				break;
			default:
		}
		return pos;
	}

	private int doList( int pos) {
		int tag = Helper.getByte( mSrc, pos);
		pos += 1;
//		mSB.append( String.format( "LIST-%d( ", tag));
		mSB.append( "LIST( ");
		pos = doArrList( pos, tag);
		mSB.append( " )");
		return pos;
	}

	private int doString( int pos) {
		int len = Helper.getShort( mSrc, pos);
		pos += 2;
		String str = Helper.getString( mSrc, pos, len);
		mSB.append( str.replace( "'", "\\'"));
		return pos + len;
	}

	private int doValue( int tag, int pos) {
		switch (tag) {
			case 0: // End
				break;
			case 1: // Byte
				mSB.append( "BYTE(");
				mSB.append( Helper.getByte( mSrc, pos));
				mSB.append( ")");
				pos += 1;
				break;
			case 2: // Short
				mSB.append( "SHORT(");
				mSB.append( Helper.getShort( mSrc, pos));
				mSB.append( ")");
				pos += 2;
				break;
			case 3: // Int
				mSB.append( "INT(");
				mSB.append( Helper.getInt( mSrc, pos));
				mSB.append( ")");
				pos += 4;
				break;
			case 4: // Long
				mSB.append( "LONG(");
				mSB.append( Helper.getLong( mSrc, pos));
				mSB.append( ")");
				pos += 8;
				break;
			case 5: // Float
				mSB.append( "FLOAT(");
				mSB.append( Helper.getFloat( mSrc, pos));
				mSB.append( ")");
				pos += 4;
				break;
			case 6: // Double
				mSB.append( "DOUBLE(");
				mSB.append( Helper.getDouble( mSrc, pos));
				mSB.append( ")");
				pos += 8;
				break;
			case 7: // Byte-Array
				mSB.append( "BYTE-ARRAY( ");
				pos = doArrByte( pos);
				mSB.append( " )");
				break;
			case 8: // String
				mSB.append( "STRING('");
				pos = doString( pos);
				mSB.append( "')");
				break;
			case 9: // List
				pos = doList( pos);
				break;
			case 10: // Compound
				mSB.append( "COMPOUND( ");
				pos = doAll( pos);
				mSB.append( " )");
				break;
			case 11: // Int-Array
				mSB.append( "INT-ARRAY( ");
				pos = doArrInt( pos);
				mSB.append( " )");
				break;
		}
		return pos;
	}

	@Override
	public String toString() {
		return mSB.toString();
	}
}
