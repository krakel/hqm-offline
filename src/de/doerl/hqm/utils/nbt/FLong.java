package de.doerl.hqm.utils.nbt;

final class FLong extends ANbt {
	private long mValue;

	FLong( String name, long value, int tag) {
		super( name, tag);
		mValue = value;
	}

	static FLong createByte( String name, int value) {
		return new FLong( name, value, 1);
	}

	static FLong createInt( String name, int value) {
		return new FLong( name, value, 3);
	}

	static FLong createLong( String name, long value) {
		return new FLong( name, value, 4);
	}

	static FLong createShort( String name, int value) {
		return new FLong( name, value, 2);
	}

	public byte toByte() {
		return (byte) mValue;
	}

	public int toInt() {
		return (int) mValue;
	}

	public long toLong() {
		return mValue;
	}

	public short toShort() {
		return (short) mValue;
	}

	@Override
	public void toString( StringBuffer sb) {
		switch (getTag()) {
			case 1:
				sb.append( "BYTE(");
				sb.append( (byte) mValue);
				sb.append( ")");
				break;
			case 2:
				sb.append( "SHORT(");
				sb.append( (short) mValue);
				sb.append( ")");
				break;
			case 3:
				sb.append( "INT(");
				sb.append( (int) mValue);
				sb.append( ")");
				break;
			case 4:
				sb.append( "LONG(");
				sb.append( mValue);
				sb.append( ")");
				break;
			default:
				break;
		}
	}
}
