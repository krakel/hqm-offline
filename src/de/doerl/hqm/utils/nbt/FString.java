package de.doerl.hqm.utils.nbt;

import de.doerl.hqm.utils.Utils;

public final class FString extends ANbt {
	public static final int ID = 8;
	private String mValue;

	FString( String name, String value) {
		super( name, ID);
		mValue = value;
	}

	public static FString create( String name, String value) {
		return new FString( name, value);
	}

	public static String to( ANbt nbt, String def) {
		try {
			FString str = (FString) nbt;
			return str.getValue();
		}
		catch (Exception ex) {
			return def;
		}
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
			FString other = (FString) obj;
			if (Utils.different( getName(), other.getName())) {
				return false;
			}
			if (Utils.different( mValue, other.mValue)) {
				return false;
			}
		}
		catch (ClassCastException ex) {
			return false;
		}
		return true;
	}

	public String getValue() {
		return mValue;
	}

	@Override
	int matcher( ANbt other) {
		if (other != null && getTag() == other.getTag() && Utils.equals( getName(), other.getName())) {
			FString o = (FString) other;
			if (Utils.equals( mValue, o.mValue)) {
				return 1;
			}
		}
		return 0;
	}

	void setValue( String value) {
		mValue = value;
	}

	@Override
	public String toString() {
		return mValue;
	}

	@Override
	public void toString( StringBuilder sb) {
		sb.append( "STRING('");
		sb.append( mValue);
		sb.append( "')");
	}
}
