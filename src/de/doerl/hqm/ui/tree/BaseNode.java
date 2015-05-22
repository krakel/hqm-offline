package de.doerl.hqm.ui.tree;

import de.doerl.hqm.base.ABase;

class BaseNode extends ANode {
	private ABase mBase;

	public BaseNode( ABase base) {
		mBase = base;
	}

	@Override
	public ABase getBase() {
		return mBase;
	}
}
