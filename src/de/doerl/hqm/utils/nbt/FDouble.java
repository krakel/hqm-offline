package de.doerl.hqm.utils.nbt;

final class FDouble extends ANbt {
	private double mValue;

	FDouble( String name, double value, int tag) {
		super( name, tag);
		mValue = value;
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
			case 3:
				sb.append( "FLOAT(");
				sb.append( (float) mValue);
				sb.append( ")");
				break;
			case 4:
				sb.append( "DOUBLE(");
				sb.append( mValue);
				sb.append( ")");
				break;
			default:
				break;
		}
	}
}
