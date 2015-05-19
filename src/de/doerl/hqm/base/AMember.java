package de.doerl.hqm.base;


public abstract class AMember<E extends ANamed> extends ANamed {
	AMember( String name) {
		super( name);
	}

	public void delete() {
		getParent().removeMember( this);
	}

	@Override
	public abstract ACategory<E> getParent();
}
