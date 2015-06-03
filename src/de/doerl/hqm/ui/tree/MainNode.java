package de.doerl.hqm.ui.tree;

import java.io.File;

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
		File src = mHqm.getSource();
		return String.valueOf( src != null ? src.getPath() : "new hqm");
	}
}
