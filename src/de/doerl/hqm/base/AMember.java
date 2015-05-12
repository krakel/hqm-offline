package de.doerl.hqm.base;

public abstract class AMember<E extends ANamed> extends ANamed {
	public final ASet<E> mParentGroup;

	AMember( ASet<E> parent, String name) {
		super( name);
		mParentGroup = parent;
	}

	@Override
	public ASet<E> getParent() {
		return mParentGroup;
	}
}
