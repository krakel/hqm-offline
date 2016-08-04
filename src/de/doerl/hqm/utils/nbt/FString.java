package de.doerl.hqm.utils.nbt;

final class FString extends ANbt {
	private String mValue;

	FString( String name, String value) {
		super( name, 8);
		mValue = value;
	}

	static FString create( String name, String value) {
		return new FString( name, value);
	}

	public String getValue() {
		return mValue;
	}

	void setValue( String value) {
		mValue = value;
	}

	@Override
	public void toString( StringBuffer sb) {
		sb.append( "STRING('");
		sb.append( mValue);
		sb.append( "')");
	}
}
