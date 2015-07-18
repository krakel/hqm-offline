package de.doerl.hqm.base;

public abstract class AMember extends AIdent implements IElement {
	AMember( String base, int id) {
		super( base, id);
	}

	@Override
	public ACategory<? extends AMember> getParent() {
		return null;
	}

	@Override
	public abstract void setName( String name);
}
