package de.doerl.hqm.utils.nbt;

import de.doerl.hqm.utils.Utils;

public final class FDouble extends ANbt {
	private double mValue;

	FDouble( String name, double value, int tag) {
		super( name, tag);
		mValue = value;
	}

	public static FDouble createDouble( double value) {
		return new FDouble( "", value, 6);
	}

	public static FDouble createDouble( String name, double value) {
		return new FDouble( name, value, 6);
	}

	public static FDouble createFloat( float value) {
		return new FDouble( "", value, 5);
	}

	public static FDouble createFloat( String name, float value) {
		return new FDouble( name, value, 5);
	}

	public static double toDouble( ANbt nbt, double def) {
		try {
			FDouble obj = (FDouble) nbt;
			return obj.asDouble();
		}
		catch (Exception ex) {
			return def;
		}
	}

	public static float toFloat( ANbt nbt, float def) {
		try {
			FDouble obj = (FDouble) nbt;
			return obj.asFloat();
		}
		catch (Exception ex) {
			return def;
		}
	}

	public double asDouble() {
		return mValue;
	}

	public float asFloat() {
		return (float) mValue;
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
			FDouble other = (FDouble) obj;
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
			case 5:
				sb.append( "FLOAT(");
				sb.append( (float) mValue);
				sb.append( ")");
				break;
			case 6:
				sb.append( "DOUBLE(");
				sb.append( mValue);
				sb.append( ")");
				break;
			default:
				break;
		}
	}
}
