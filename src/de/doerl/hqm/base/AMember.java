package de.doerl.hqm.base;

public abstract class AMember<E extends ANamed> extends ANamed {
	AMember( String name) {
		super( name);
	}

	@Override
	public abstract ASet<E> getParent();
}
