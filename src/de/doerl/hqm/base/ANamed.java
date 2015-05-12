package de.doerl.hqm.base;

public abstract class ANamed extends ABase {
	private String mName;

	ANamed( String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setName( String name) {
		mName = name;
	}
}
