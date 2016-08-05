package de.doerl.hqm.utils.nbt;

final class FDouble extends ANbt {
	private double mValue;

	FDouble( String name, double value, int tag) {
		super( name, tag);
		mValue = value;
	}

	static FDouble createDouble( double value) {
		return new FDouble( "", value, 6);
	}

	static FDouble createDouble( String name, double value) {
		return new FDouble( name, value, 6);
	}

	static FDouble createFloat( float value) {
		return new FDouble( "", value, 5);
	}

	static FDouble createFloat( String name, float value) {
		return new FDouble( name, value, 5);
	}

	public double toDouble() {
		return mValue;
	}

	public float toFloat() {
		return (float) mValue;
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
