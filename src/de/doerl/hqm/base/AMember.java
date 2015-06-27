package de.doerl.hqm.base;

public abstract class AMember extends ANamed implements IElement {
	AMember( String name) {
		super( name);
	}

	@Override
	public ACategory<? extends AMember> getParent() {
		return null;
	}

	@Override
	public boolean isInformation() {
		return false;
	}

	@Override
	public void setInformation( boolean information) {
	}
}
