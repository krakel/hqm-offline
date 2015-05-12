package de.doerl.hqm.questX.minecraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import de.doerl.hqm.utils.Helper;

public class NBTTagCompound {
	byte[] mBytes;
	String mText;

	public NBTTagCompound( byte[] bytes) throws IOException {
		mBytes = bytes;
		mText = parse( decompress( bytes), 0);
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
		parseValues( sb, src, pos);
		return sb.toString();
	}

	private static int parseByteArr( StringBuilder sb, byte[] src, int pos) {
		int count = Helper.getInt( src, pos);
		pos += 4;
		sb.append( "[");
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				sb.append( ", ");
			}
			sb.append( Helper.getByte( src, pos));
			pos += 1;
		}
		sb.append( "]");
		return pos;
	}

	private static int parseIntArr( StringBuilder sb, byte[] src, int pos) {
		int count = Helper.getInt( src, pos);
		pos += 4;
		sb.append( "[");
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				sb.append( ", ");
			}
			sb.append( Helper.getInt( src, pos));
			pos += 4;
		}
		sb.append( "]");
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
				sb.append( ":");
				break;
			default: // End
				break;
		}
		return pos;
	}

	private static int parseListArr( StringBuilder sb, byte[] src, int pos) {
		int tag = Helper.getByte( src, pos);
		pos += 1;
		int count = Helper.getInt( src, pos);
		pos += 4;
		sb.append( "[");
		for (int i = 0; i < count; ++i) {
			if (i > 0) {
				sb.append( ", ");
			}
			pos = parseValue( sb, tag, src, pos);
		}
		sb.append( "]");
		return pos;
	}

	private static int parseString( StringBuilder sb, byte[] src, int pos) {
		int len = Helper.getShort( src, pos);
		pos += 2;
		sb.append( Helper.getString( src, pos, len));
		return pos + len;
	}

	private static void parseTag( StringBuilder sb, int tag) {
		switch (tag) {
			case 0: // End
				sb.append( "END ");
				break;
			case 1: // Byte
				sb.append( "BYTE ");
				break;
			case 2: // Short
				sb.append( "SHORT ");
				break;
			case 3: // Int
				sb.append( "INT ");
				break;
			case 4: // Long
				sb.append( "LONG ");
				break;
			case 5: // Float
				sb.append( "FLOAT ");
				break;
			case 6: // Double
				sb.append( "DOUBLE ");
				break;
			case 7: // Byte-Array
				sb.append( "BYTE-ARRAY ");
				break;
			case 8: // String
				sb.append( "STRING ");
				break;
			case 9: // List
				sb.append( "LIST ");
				break;
			case 10: // Compound
				sb.append( "COMPOUND ");
				break;
			case 11: // Int-Array
				sb.append( "INT-ARRAY ");
				break;
		}
	}

	private static int parseValue( StringBuilder sb, int tag, byte[] src, int pos) {
		switch (tag) {
			case 0: // End
				break;
			case 1: // Byte
				sb.append( Helper.getByte( src, pos));
				sb.append( ", ");
				pos += 1;
				break;
			case 2: // Short
				sb.append( Helper.getShort( src, pos));
				sb.append( ", ");
				pos += 2;
				break;
			case 3: // Int
				sb.append( Helper.getInt( src, pos));
				sb.append( ", ");
				pos += 4;
				break;
			case 4: // Long
				sb.append( Helper.getLong( src, pos));
				sb.append( ", ");
				pos += 8;
				break;
			case 5: // Float
				sb.append( Helper.getFloat( src, pos));
				sb.append( ", ");
				pos += 4;
				break;
			case 6: // Double
				sb.append( Helper.getDouble( src, pos));
				sb.append( ", ");
				pos += 8;
				break;
			case 7: // Byte-Array
				pos = parseByteArr( sb, src, pos);
				sb.append( ", ");
				break;
			case 8: // String
				pos = parseString( sb, src, pos);
				sb.append( ", ");
				break;
			case 9: // List
				pos = parseListArr( sb, src, pos);
				sb.append( ", ");
				break;
			case 10: // Compound
				sb.append( "{ ");
				pos = parseValues( sb, src, pos);
				sb.append( "END } ");
				break;
			case 11: // Int-Array
				pos = parseIntArr( sb, src, pos);
				sb.append( ", ");
				break;
		}
		return pos;
	}

	private static int parseValues( StringBuilder sb, byte[] src, int pos) {
		while (pos >= 0 && pos < src.length) {
			int tag = Helper.getByte( src, pos);
			pos += 1;
			if (tag == 0) {
				break;
			}
			parseTag( sb, tag);
			pos = parseKey( sb, tag, src, pos);
			pos = parseValue( sb, tag, src, pos);
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
		int p1 = mText.indexOf( ':', pos);
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
		if (mBytes != null) {
			return mText;
		}
		return "NBTTagCompound";
	}
}
