package de.doerl.hqm.base;

public abstract class ANamed extends ABase {
	ANamed() {
	}

	@Override
	public FHqm getHqm() {
		return getParent().getHqm();
	}

	public abstract String getName();

	public abstract void setName( String name);
}
