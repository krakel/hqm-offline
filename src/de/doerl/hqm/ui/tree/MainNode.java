package de.doerl.hqm.ui.tree;

import de.doerl.hqm.base.FHqm;

class MainNode extends ANode {
	private FHqm mHqm;

	public MainNode( FHqm hqm) {
		mHqm = hqm;
	}

	@Override
	public FHqm getBase() {
		return mHqm;
	}

	@Override
	public String toString() {
		return mHqm.mName != null ? mHqm.mName : "new hqm";
	}
}
