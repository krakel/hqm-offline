package de.doerl.hqm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Nbt {
	private byte[] mBytes;
	private String mText;

	public Nbt( byte[] bytes) throws IOException {
		mBytes = decompress( bytes);
		mText = parse( mBytes, 0);
	}

	private static byte[] decompress( byte[] bytes) {
		GZIPInputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			is = new GZIPInputStream( new ByteArrayInputStream( bytes));
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read( buffer)) != -1) {
				os.write( buffer, 0, len);
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return os.toByteArray();
	}

	private static String parse( byte[] src, int pos) {
		StringBuilder sb = new StringBuilder();
		parseAll( sb, src, pos);
		return sb.toString();
	}

	private static int parseAll( StringBuilder sb, byte[] src, int pos) {
		while (pos >= 0 && pos < src.length) {
			int tag = Helper.getByte( src, pos);
			pos += 1;
			if (tag == 0) {
				break;
			}
			pos = parseKey( sb, tag, src, pos);
			pos = parseValue( sb, tag, src, pos);
			if (pos < src.length) {
				sb.append( ", ");
			}
		}
		return pos;
	}

	private static int parseArrByte( StringBuilder sb, byte[] src, int pos) {
		int count = Helper.getInt( src, pos);
		pos += 4;
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				sb.append( ", ");
			}
			sb.append( Helper.getByte( src, pos));
			pos += 1;
		}
		return pos;
	}

	private static int parseArrInt( StringBuilder sb, byte[] src, int pos) {
		int count = Helper.getInt( src, pos);
		pos += 4;
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				sb.append( ", ");
			}
			sb.append( Helper.getInt( src, pos));
			pos += 4;
		}
		return pos;
	}

	private static int parseArrList( StringBuilder sb, byte[] src, int pos) {
		int tag = Helper.getByte( src, pos);
		pos += 1;
		int count = Helper.getInt( src, pos);
		pos += 4;
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				sb.append( ", ");
			}
			pos = parseValue( sb, tag, src, pos);
		}
		return pos;
	}

	private static int parseKey( StringBuilder sb, int tag, byte[] src, int pos) {
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
				pos = parseString( sb, src, pos);
				sb.append( "=");
				break;
			default:
		}
		return pos;
	}

	private static int parseString( StringBuilder sb, byte[] src, int pos) {
		int len = Helper.getShort( src, pos);
		pos += 2;
		sb.append( Helper.getString( src, pos, len));
		return pos + len;
	}

	private static int parseValue( StringBuilder sb, int tag, byte[] src, int pos) {
		switch (tag) {
			case 0: // End
				sb.append( "END");
				break;
			case 1: // Byte
				sb.append( "BYTE(");
				sb.append( Helper.getByte( src, pos));
				sb.append( ")");
				pos += 1;
				break;
			case 2: // Short
				sb.append( "SHORT(");
				sb.append( Helper.getShort( src, pos));
				sb.append( ")");
				pos += 2;
				break;
			case 3: // Int
				sb.append( "INT(");
				sb.append( Helper.getInt( src, pos));
				sb.append( ")");
				pos += 4;
				break;
			case 4: // Long
				sb.append( "LONG(");
				sb.append( Helper.getLong( src, pos));
				sb.append( ")");
				pos += 8;
				break;
			case 5: // Float
				sb.append( "FLOAT(");
				sb.append( Helper.getFloat( src, pos));
				sb.append( ")");
				pos += 4;
				break;
			case 6: // Double
				sb.append( "DOUBLE(");
				sb.append( Helper.getDouble( src, pos));
				sb.append( ")");
				pos += 8;
				break;
			case 7: // Byte-Array
				sb.append( "BYTE-ARRAY[ ");
				pos = parseArrByte( sb, src, pos);
				sb.append( " ]");
				break;
			case 8: // String
				sb.append( "STRING(");
				pos = parseString( sb, src, pos);
				sb.append( ")");
				break;
			case 9: // List
				sb.append( "LIST[ ");
				pos = parseArrList( sb, src, pos);
				sb.append( " ]");
				break;
			case 10: // Compound
				sb.append( "COMPOUND{ ");
				pos = parseAll( sb, src, pos);
				sb.append( "END }");
				break;
			case 11: // Int-Array
				sb.append( "INT-ARRAY[ ");
				pos = parseArrInt( sb, src, pos);
				sb.append( " ]");
				break;
		}
		return pos;
	}

	public byte[] getBytes() {
		return mBytes;
	}

	public String getValue( String key) {
		int pos = mText.indexOf( key);
		if (pos < 0) {
			return "";
		}
		int p1 = mText.indexOf( '=', pos);
		if (p1 < 0) {
			return "";
		}
		int p2 = mText.indexOf( ',', p1);
		if (p2 < 0) {
			return "";
		}
		return mText.substring( p1 + 1, p2);
	}

	@Override
	public String toString() {
		return mText;
	}
}
