package de.doerl.hqm.base;

public abstract class ANamed extends ABase {
	public FParameterString mName = new FParameterString( this);

	ANamed( String name) {
		mName.mValue = name;
	}
}
