package de.doerl.hqm.ui.tree;

import de.doerl.hqm.base.ANamed;

class NamedNode extends ANode {
	private ANamed mNamed;

	public NamedNode( ANamed named) {
		mNamed = named;
	}

	@Override
	public ANamed getBase() {
		return mNamed;
	}

	@Override
	public String toString() {
		return String.valueOf( mNamed.getName());
	}
}
