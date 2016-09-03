package de.doerl.hqm.utils.nbt;

public abstract class ANbt {
	public static final int ID_END = 0;
	public static final int ID_BYTE = 1;
	public static final int ID_SHORT = 2;
	public static final int ID_INT = 3;
	public static final int ID_LONG = 4;
	public static final int ID_FLOAT = 5;
	public static final int ID_DOUBLE = 6;
	public static final int ID_BYTE_ARRAY = 7;
	public static final int ID_STRING = 8;
	public static final int ID_LIST = 9;
	public static final int ID_COMPOUND = 10;
	public static final int ID_INT_ARRAY = 11;
	private String mName;
	private int mTag;

	ANbt( String name, int tag) {
		mName = name;
		mTag = tag;
	}

	static byte getByte( byte[] src, int pos) {
		try {
			return src[pos];
		}
		catch (IndexOutOfBoundsException ex) {
			return 0;
		}
	}

	static double getDouble( byte[] src, int pos) {
		return Double.longBitsToDouble( getLong( src, pos));
	}

	static float getFloat( byte[] src, int pos) {
		return Float.intBitsToFloat( getInt( src, pos));
	}

	static int getInt( byte[] src, int pos) {
		int result = 0;
		int end = Math.min( pos + 4, src.length);
		for (int i = Math.max( pos, 0); i < end; ++i) {
			result <<= 8;
			result |= src[i] & 0xFF;
		}
		return result;
	}

	static long getLong( byte[] src, int pos) {
		long result = 0L;
		int end = Math.min( pos + 8, src.length);
		for (int i = Math.max( pos, 0); i < end; ++i) {
			result <<= 8;
			result |= src[i] & 0xFFL;
		}
		return result;
	}

	static int getShort( byte[] src, int pos) {
		short result = 0;
		int end = Math.min( pos + 2, src.length);
		for (int i = Math.max( pos, 0); i < end; ++i) {
			result <<= 8;
			result |= src[i] & 0xFF;
		}
		return result;
	}

	static String getString( byte[] src, int off, int len) {
		return new String( src, off, len);
	}

	public String getName() {
		return mName;
	}

	public int getTag() {
		return mTag;
	}

	abstract int matcher( ANbt other);

	void setName( String name) {
		mName = name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString( sb);
		return sb.toString();
	}

	abstract void toString( StringBuilder sb);
}
