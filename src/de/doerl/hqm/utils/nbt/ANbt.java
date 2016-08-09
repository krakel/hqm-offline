package de.doerl.hqm.utils.nbt;

public abstract class ANbt implements INbt {
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

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public int getTag() {
		return mTag;
	}

	abstract int matcher( ANbt other);

	void setName( String name) {
		mName = name;
	}
}
