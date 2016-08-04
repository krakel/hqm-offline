package de.doerl.hqm.utils.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import de.doerl.hqm.utils.Utils;

public class NbtReader {
	private static final Logger LOGGER = Logger.getLogger( NbtReader.class.getName());
	private StringBuilder mSB = new StringBuilder();
	private byte[] mSrc;

	private NbtReader( byte[] src) {
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

	static ArrayList<String> parseAsList( byte[] arr) {
		ArrayList<String> res = new ArrayList<>();
		NbtReader rdr = new NbtReader( arr);
		rdr.doAllList( res);
		return res;
	}

	static String parseAsString( byte[] arr) {
		NbtReader rdr = new NbtReader( arr);
		rdr.doAll( 0);
		return rdr.toString();
	}

	private static byte[] read( InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		decompress( in, out);
		return out.toByteArray();
	}

	public static ArrayList<String> readAsList( InputStream in) {
		byte[] src = read( in);
		return parseAsList( src);
	}

	public static String readAsString( byte[] arr) {
		byte[] src = read( new ByteArrayInputStream( arr));
		return parseAsString( src);
	}

	private int doAll( int pos) {
		boolean comma = false;
		while (pos >= 0 && pos < mSrc.length) {
			int tag = ANbt.getByte( mSrc, pos);
			if (tag == 0) {
				break;
			}
			if (comma) {
				mSB.append( ", ");
			}
			pos = doKey( tag, pos + 1);
			pos = doValue( tag, pos);
			comma = true;
		}
		return pos;
	}

	private void doAllList( ArrayList<String> res) {
		int pos = 0;
		if (ANbt.getByte( mSrc, pos) == 10) {
			pos = doStringIgnore( pos + 1);
			if (ANbt.getByte( mSrc, pos) == 9) {
				pos = doStringIgnore( pos + 1);
				if (ANbt.getByte( mSrc, pos) == 10) {
					pos += 1;
					int count = ANbt.getInt( mSrc, pos);
					pos += 4;
					res.ensureCapacity( count);
					for (int i = 0; i < count; ++i) {
						pos = doValue( 10, pos);
						res.add( mSB.toString());
						mSB.setLength( 0);
					}
				}
			}
		}
	}

	private int doArrByte( int pos) {
		int count = ANbt.getInt( mSrc, pos);
		pos += 4;
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				mSB.append( ", ");
			}
			mSB.append( ANbt.getByte( mSrc, pos));
			pos += 1;
		}
		return pos;
	}

	private int doArrInt( int pos) {
		int count = ANbt.getInt( mSrc, pos);
		pos += 4;
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				mSB.append( ", ");
			}
			mSB.append( ANbt.getInt( mSrc, pos));
			pos += 4;
		}
		return pos;
	}

	private int doArrList( int pos, int tag) {
		int count = ANbt.getInt( mSrc, pos);
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
		mSB.append( "LIST( ");
		int tag = ANbt.getByte( mSrc, pos);
		pos = doArrList( pos + 1, tag);
		mSB.append( " )");
		return pos;
	}

	private int doString( int pos) {
		int len = ANbt.getShort( mSrc, pos);
		pos += 2;
		String str = ANbt.getString( mSrc, pos, len);
		mSB.append( str.replace( "'", "\\'"));
		return pos + len;
	}

	private int doStringIgnore( int pos) {
		int len = ANbt.getShort( mSrc, pos);
		return pos + len + 2;
	}

	private int doValue( int tag, int pos) {
		switch (tag) {
			case 0: // End
				break;
			case 1: // Byte
				mSB.append( "BYTE(");
				mSB.append( ANbt.getByte( mSrc, pos));
				mSB.append( ")");
				pos += 1;
				break;
			case 2: // Short
				mSB.append( "SHORT(");
				mSB.append( ANbt.getShort( mSrc, pos));
				mSB.append( ")");
				pos += 2;
				break;
			case 3: // Int
				mSB.append( "INT(");
				mSB.append( ANbt.getInt( mSrc, pos));
				mSB.append( ")");
				pos += 4;
				break;
			case 4: // Long
				mSB.append( "LONG(");
				mSB.append( ANbt.getLong( mSrc, pos));
				mSB.append( ")");
				pos += 8;
				break;
			case 5: // Float
				mSB.append( "FLOAT(");
				mSB.append( ANbt.getFloat( mSrc, pos));
				mSB.append( ")");
				pos += 4;
				break;
			case 6: // Double
				mSB.append( "DOUBLE(");
				mSB.append( ANbt.getDouble( mSrc, pos));
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
