package de.doerl.hqm.base;

public abstract class AMember extends AUUID implements IElement {
	AMember( String base, int id) {
		super( base, id);
	}

	@Override
	public ACategory<? extends AMember> getParent() {
		return null;
	}
}
