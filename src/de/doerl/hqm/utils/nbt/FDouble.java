package de.doerl.hqm.utils.nbt;

import de.doerl.hqm.utils.Utils;

public final class FDouble extends ANbt {
	private double mValue;

	FDouble( String name, double value, int tag) {
		super( name, tag);
		mValue = value;
	}

	public static FDouble createDouble( double value) {
		return new FDouble( "", value, ANbt.ID_DOUBLE);
	}

	public static FDouble createDouble( String name, double value) {
		return new FDouble( name, value, ANbt.ID_DOUBLE);
	}

	public static FDouble createFloat( float value) {
		return new FDouble( "", value, ANbt.ID_FLOAT);
	}

	public static FDouble createFloat( String name, float value) {
		return new FDouble( name, value, ANbt.ID_FLOAT);
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
	int matcher( ANbt other) {
		if (other != null && getTag() == other.getTag() && Utils.equals( getName(), other.getName())) {
			FDouble o = (FDouble) other;
			if (mValue == o.mValue) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public void toString( StringBuilder sb) {
		switch (getTag()) {
			case ANbt.ID_FLOAT:
				sb.append( (float) mValue);
				sb.append( "f");
				break;
			case 6:
				sb.append( mValue);
				sb.append( "d");
				break;
			default:
				break;
		}
	}
}
