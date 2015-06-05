package de.doerl.hqm.base;

public abstract class AMember<E extends ANamed> extends ANamed implements IElement {
	AMember( String name) {
		super( name);
	}

	@Override
	public abstract ACategory<E> getParent();

	public boolean isFirst() {
		return ABase.isFirst( getParent().mArr, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( getParent().mArr, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( getParent().mArr, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( getParent().mArr, this);
	}

	public void remove() {
		ABase.remove( getParent().mArr, this);
	}
}
