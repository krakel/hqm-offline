package de.doerl.hqm.utils.nbt;

import de.doerl.hqm.utils.Utils;

public final class FLong extends ANbt {
	private long mValue;

	FLong( String name, long value, int tag) {
		super( name, tag);
		mValue = value;
	}

	public static FLong createByte( int value) {
		return new FLong( "", value, 1);
	}

	public static FLong createByte( String name, int value) {
		return new FLong( name, value, 1);
	}

	public static FLong createInt( int value) {
		return new FLong( "", value, 3);
	}

	public static FLong createInt( String name, int value) {
		return new FLong( name, value, 3);
	}

	public static FLong createLong( long value) {
		return new FLong( "", value, 4);
	}

	public static FLong createLong( String name, long value) {
		return new FLong( name, value, 4);
	}

	public static FLong createShort( int value) {
		return new FLong( "", value, 2);
	}

	public static FLong createShort( String name, int value) {
		return new FLong( name, value, 2);
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
