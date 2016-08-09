package de.doerl.hqm.utils.nbt;

import de.doerl.hqm.utils.Utils;

public final class FLong extends ANbt {
	public static final int ID_BYTE = 1;
	public static final int ID_SHORT = 2;
	public static final int ID_INT = 3;
	public static final int ID_LONG = 4;
	private long mValue;

	FLong( String name, long value, int tag) {
		super( name, tag);
		mValue = value;
	}

	public static FLong createByte( int value) {
		return new FLong( "", value, ID_BYTE);
	}

	public static FLong createByte( String name, int value) {
		return new FLong( name, value, ID_BYTE);
	}

	public static FLong createInt( int value) {
		return new FLong( "", value, ID_INT);
	}

	public static FLong createInt( String name, int value) {
		return new FLong( name, value, ID_INT);
	}

	public static FLong createLong( long value) {
		return new FLong( "", value, ID_LONG);
	}

	public static FLong createLong( String name, long value) {
		return new FLong( name, value, ID_LONG);
	}

	public static FLong createShort( int value) {
		return new FLong( "", value, ID_SHORT);
	}

	public static FLong createShort( String name, int value) {
		return new FLong( name, value, ID_SHORT);
	}

	public static byte toByte( ANbt nbt, byte def) {
		try {
			FLong obj = (FLong) nbt;
			return obj.asByte();
		}
		catch (Exception ex) {
			return def;
		}
	}

	public static int toInt( ANbt nbt, int def) {
		try {
			FLong obj = (FLong) nbt;
			return obj.asInt();
		}
		catch (Exception ex) {
			return def;
		}
	}

	public static long toLong( ANbt nbt, long def) {
		try {
			FLong obj = (FLong) nbt;
			return obj.asLong();
		}
		catch (Exception ex) {
			return def;
		}
	}

	public static short toShort( ANbt nbt, short def) {
		try {
			FLong obj = (FLong) nbt;
			return obj.asShort();
		}
		catch (Exception ex) {
			return def;
		}
	}

	public byte asByte() {
		return (byte) mValue;
	}

	public int asInt() {
		return (int) mValue;
	}

	public long asLong() {
		return mValue;
	}

	public short asShort() {
		return (short) mValue;
	}

	@Override
	public boolean equals( Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		try {
			FLong other = (FLong) obj;
			if (Utils.different( getName(), other.getName())) {
				return false;
			}
			if (mValue != other.mValue) {
				return false;
			}
		}
		catch (ClassCastException ex) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Long.toString( mValue);
	}

	@Override
	public void toString( StringBuilder sb) {
		switch (getTag()) {
			case ID_BYTE:
				sb.append( "BYTE(");
				sb.append( (byte) mValue);
				sb.append( ")");
				break;
			case ID_SHORT:
				sb.append( "SHORT(");
				sb.append( (short) mValue);
				sb.append( ")");
				break;
			case ID_INT:
				sb.append( "INT(");
				sb.append( (int) mValue);
				sb.append( ")");
				break;
			case ID_LONG:
				sb.append( "LONG(");
				sb.append( mValue);
				sb.append( ")");
				break;
			default:
				break;
		}
	}
}
