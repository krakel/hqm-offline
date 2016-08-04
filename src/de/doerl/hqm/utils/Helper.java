/*
 * Source File: MBytes.java
 * Copyright (c) 2003-2004 by Uwe Doerl
 *
 * $Created:    8/16/04 (2:38:34 PM) by doerl $
 * Last Change: 11/1/04 (2:51:10 PM) by Doerl
 */
package de.doerl.hqm.utils;

public final class Helper {
	private static final boolean CUT_OUTPUT = true;
	private static final int CUT_LENGTH = 32;
	private static final char[] HEX = "0123456789ABCDEF".toCharArray();
	public static final String NL = "\n";

	public static byte[] fromHex( String src) {
		byte[] result = new byte[(src.length() + 1) / 2];
		int pos = src.length() - 2;
		for (int i = result.length - 1; i >= 0; --i) {
			result[i] = getHexByte( src, pos);
			pos -= 2;
		}
		return result;
	}

	private static byte getHexByte( String src, int pos) {
		int result = 0;
		if (pos >= 0) {
			result = getHexNibble( src.charAt( pos)) << 4;
		}
		if (pos >= -1) {
			result |= getHexNibble( src.charAt( pos + 1));
		}
		return (byte) result;
	}

	private static int getHexNibble( char ch) {
		if (ch >= 'a' && ch <= 'f') {
			return ch + 10 - 'a';
		}
		if (ch >= 'A' && ch <= 'F') {
			return ch + 10 - 'A';
		}
		return ch - '0';
	}

	public static int getUByte( byte[] src, int pos) {
		try {
			return src[pos] & 0xFF;
		}
		catch (IndexOutOfBoundsException ex) {
			return 0;
		}
	}

	public static long getUInt( byte[] src, int pos) {
		long result = 0;
		int end = Math.min( pos + 4, src.length);
		for (int i = Math.max( pos, 0); i < end; ++i) {
			result <<= 8;
			result |= src[i] & 0xFFL;
		}
		return result;
	}

	public static int getUShort( byte[] src, int pos) {
		int result = 0;
		int end = Math.min( pos + 2, src.length);
		for (int i = Math.max( pos, 0); i < end; ++i) {
			result <<= 8;
			result |= src[i] & 0xFF;
		}
		return result;
	}

	public static String join( Object[] arr, int start) {
		StringBuffer sb = new StringBuffer();
		for (int i = start; i < arr.length; ++i) {
			if (i > start) {
				sb.append( ", ");
			}
			sb.append( arr[i].toString());
		}
		return sb.toString();
	}

	public static String repeat( int tabs, String text) {
		StringBuffer sb = new StringBuffer();
		while (tabs > 0) {
			sb.append( text);
			//                      sb.append("      ");
			tabs--;
		}
		return sb.toString();
	}

	public static String toHex( byte value) {
		StringBuffer sb = new StringBuffer();
		for (int i = 4; i >= 0; i -= 4) {
			sb.append( HEX[value >>> i & 0xF]);
		}
		return sb.toString();
	}

	public static String toHex( byte[] src) {
		return toHex( src, 0, src.length);
	}

	public static String toHex( byte[] src, int off, int len) {
		StringBuffer sb = new StringBuffer();
		boolean cutOutput = CUT_OUTPUT && len > CUT_LENGTH;
		if (cutOutput) {
			len = CUT_LENGTH;
		}
		for (int i = 0; i < len; ++i) {
			sb.append( toHex( src[off + i]));
			sb.append( ' ');
		}
		if (cutOutput) {
			sb.append( "...");
		}
		return sb.toString();
	}

	public static String toHex( int value) {
		StringBuffer sb = new StringBuffer();
		for (int i = 28; i >= 0; i -= 4) {
			sb.append( HEX[value >>> i & 0xF]);
		}
		return sb.toString();
	}

	public static String toHex( long value) {
		StringBuffer sb = new StringBuffer();
		for (int i = 60; i >= 0; i -= 4) {
			sb.append( HEX[(int) (value >>> i & 0xFL)]);
		}
		return sb.toString();
	}

	public static String toHex( short value) {
		StringBuffer sb = new StringBuffer();
		for (int i = 12; i >= 0; i -= 4) {
			sb.append( HEX[value >>> i & 0xF]);
		}
		return sb.toString();
	}

	public static String toString( int[] src) {
		return toString( src, 0, src.length);
	}

	private static String toString( int[] src, int off, int len) {
		StringBuffer sb = new StringBuffer();
		boolean cutOutput = CUT_OUTPUT && len > CUT_LENGTH;
		if (cutOutput) {
			len = CUT_LENGTH;
		}
		for (int i = 0; i < len; ++i) {
			sb.append( src[off + i]);
			sb.append( ' ');
		}
		if (cutOutput) {
			sb.append( "...");
		}
		return sb.toString();
	}
}
